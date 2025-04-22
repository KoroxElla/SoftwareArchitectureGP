package com.example.part2.data.dao;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile CourseDao _courseDao;

  private volatile StudentDao _studentDao;

  private volatile CourseStudentDao _courseStudentDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Course` (`courseId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `courseCode` TEXT, `courseName` TEXT, `lecturerName` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Student` (`studentId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `email` TEXT, `userName` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `CourseStudentCrossRef` (`courseId` INTEGER NOT NULL, `studentId` INTEGER NOT NULL, PRIMARY KEY(`courseId`, `studentId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '74c75b9d04cf477a85c03c089c6ab76c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `Course`");
        db.execSQL("DROP TABLE IF EXISTS `Student`");
        db.execSQL("DROP TABLE IF EXISTS `CourseStudentCrossRef`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCourse = new HashMap<String, TableInfo.Column>(4);
        _columnsCourse.put("courseId", new TableInfo.Column("courseId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourse.put("courseCode", new TableInfo.Column("courseCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourse.put("courseName", new TableInfo.Column("courseName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourse.put("lecturerName", new TableInfo.Column("lecturerName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourse = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCourse = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCourse = new TableInfo("Course", _columnsCourse, _foreignKeysCourse, _indicesCourse);
        final TableInfo _existingCourse = TableInfo.read(db, "Course");
        if (!_infoCourse.equals(_existingCourse)) {
          return new RoomOpenHelper.ValidationResult(false, "Course(com.example.part2.data.entities.Course).\n"
                  + " Expected:\n" + _infoCourse + "\n"
                  + " Found:\n" + _existingCourse);
        }
        final HashMap<String, TableInfo.Column> _columnsStudent = new HashMap<String, TableInfo.Column>(4);
        _columnsStudent.put("studentId", new TableInfo.Column("studentId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudent.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudent.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsStudent.put("userName", new TableInfo.Column("userName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStudent = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStudent = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStudent = new TableInfo("Student", _columnsStudent, _foreignKeysStudent, _indicesStudent);
        final TableInfo _existingStudent = TableInfo.read(db, "Student");
        if (!_infoStudent.equals(_existingStudent)) {
          return new RoomOpenHelper.ValidationResult(false, "Student(com.example.part2.data.entities.Student).\n"
                  + " Expected:\n" + _infoStudent + "\n"
                  + " Found:\n" + _existingStudent);
        }
        final HashMap<String, TableInfo.Column> _columnsCourseStudentCrossRef = new HashMap<String, TableInfo.Column>(2);
        _columnsCourseStudentCrossRef.put("courseId", new TableInfo.Column("courseId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourseStudentCrossRef.put("studentId", new TableInfo.Column("studentId", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourseStudentCrossRef = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCourseStudentCrossRef = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCourseStudentCrossRef = new TableInfo("CourseStudentCrossRef", _columnsCourseStudentCrossRef, _foreignKeysCourseStudentCrossRef, _indicesCourseStudentCrossRef);
        final TableInfo _existingCourseStudentCrossRef = TableInfo.read(db, "CourseStudentCrossRef");
        if (!_infoCourseStudentCrossRef.equals(_existingCourseStudentCrossRef)) {
          return new RoomOpenHelper.ValidationResult(false, "CourseStudentCrossRef(com.example.part2.data.entities.CourseStudentCrossRef).\n"
                  + " Expected:\n" + _infoCourseStudentCrossRef + "\n"
                  + " Found:\n" + _existingCourseStudentCrossRef);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "74c75b9d04cf477a85c03c089c6ab76c", "66cbaebb431b4270ac11a86aa2ff9f6e");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Course","Student","CourseStudentCrossRef");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Course`");
      _db.execSQL("DELETE FROM `Student`");
      _db.execSQL("DELETE FROM `CourseStudentCrossRef`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CourseDao.class, CourseDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(StudentDao.class, StudentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CourseStudentDao.class, CourseStudentDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CourseDao courseDao() {
    if (_courseDao != null) {
      return _courseDao;
    } else {
      synchronized(this) {
        if(_courseDao == null) {
          _courseDao = new CourseDao_Impl(this);
        }
        return _courseDao;
      }
    }
  }

  @Override
  public StudentDao studentDao() {
    if (_studentDao != null) {
      return _studentDao;
    } else {
      synchronized(this) {
        if(_studentDao == null) {
          _studentDao = new StudentDao_Impl(this);
        }
        return _studentDao;
      }
    }
  }

  @Override
  public CourseStudentDao courseStudentDao() {
    if (_courseStudentDao != null) {
      return _courseStudentDao;
    } else {
      synchronized(this) {
        if(_courseStudentDao == null) {
          _courseStudentDao = new CourseStudentDao_Impl(this);
        }
        return _courseStudentDao;
      }
    }
  }
}
