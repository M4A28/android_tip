Foreign keys (FK) in Room, the SQLite abstraction library for Android, are used to establish relationships between tables. Room simplifies the management of foreign key constraints through annotations and handles referential integrity. Here's a detailed explanation of how to implement foreign keys in Room:

### Step-by-Step Implementation

1. **Define Entities**: Create your tables (entities) and specify the foreign key relationships using annotations.

2. **Annotate Foreign Keys**: Use `@ForeignKey` annotation to define foreign keys in your entities.

### Example Implementation

Let's implement the `Drug` and `Supplier` tables from the previous schema with a foreign key relationship in Room.

#### 1. Add Dependencies
Make sure you have Room dependencies in your `build.gradle` file.

```gradle
implementation "androidx.room:room-runtime:2.4.0"
kapt "androidx.room:room-compiler:2.4.0"
implementation "androidx.room:room-ktx:2.4.0"
```

#### 2. Define Entities

##### Supplier Entity
```kotlin
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class Supplier(
    @PrimaryKey(autoGenerate = true) val supplierId: Int,
    val name: String,
    val contactInfo: String
)
```

##### Drug Entity
```kotlin
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "drug",
    foreignKeys = [ForeignKey(
        entity = Supplier::class,
        parentColumns = ["supplierId"],
        childColumns = ["supplierId"],
        onDelete = ForeignKey.CASCADE // Optional: Handle delete behavior
    )]
)
data class Drug(
    @PrimaryKey(autoGenerate = true) val drugId: Int,
    val name: String,
    val description: String,
    val price: Double,
    val expiryDate: String,
    val supplierId: Int // Foreign key to Supplier table
)
```

### 3. Define DAOs

##### SupplierDao
```kotlin
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SupplierDao {
    @Insert
    suspend fun insertSupplier(supplier: Supplier)

    @Query("SELECT * FROM supplier")
    suspend fun getAllSuppliers(): List<Supplier>
}
```

##### DrugDao
```kotlin
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DrugDao {
    @Insert
    suspend fun insertDrug(drug: Drug)

    @Query("SELECT * FROM drug")
    suspend fun getAllDrugs(): List<Drug>
}
```

### 4. Create the Database

```kotlin
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Supplier::class, Drug::class], version = 1)
abstract class PharmacyDatabase : RoomDatabase() {
    abstract fun supplierDao(): SupplierDao
    abstract fun drugDao(): DrugDao
}
```

### 5. Initialize the Database

```kotlin
import android.content.Context
import androidx.room.Room

object DatabaseInstance {
    private var INSTANCE: PharmacyDatabase? = null

    fun getDatabase(context: Context): PharmacyDatabase {
        if (INSTANCE == null) {
            synchronized(PharmacyDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    PharmacyDatabase::class.java,
                    "pharmacy_database"
                ).build()
            }
        }
        return INSTANCE!!
    }
}
```

### Explanation of Foreign Key Handling in Room

- **`@ForeignKey` Annotation**: This is used in the child entity to define a foreign key relationship with the parent entity.
  - `entity`: Specifies the parent entity class.
  - `parentColumns`: Defines the column(s) in the parent entity that the foreign key references.
  - `childColumns`: Defines the column(s) in the child entity that act as the foreign key.
  - `onDelete`: Optional. Specifies the behavior when a referenced row in the parent table is deleted. Options include `CASCADE`, `SET NULL`, etc.

- **Referential Integrity**: Room enforces foreign key constraints, ensuring that the data integrity rules are maintained. For example, you cannot insert a `Drug` without a valid `SupplierId` that exists in the `Supplier` table.

- **Handling Deletes**: The `onDelete = ForeignKey.CASCADE` option ensures that when a `Supplier` is deleted, all related `Drug` entries are also deleted, maintaining referential integrity.

By defining foreign keys in Room, you ensure that your database maintains proper relationships and data integrity, reducing the likelihood of orphaned rows and inconsistent data.
