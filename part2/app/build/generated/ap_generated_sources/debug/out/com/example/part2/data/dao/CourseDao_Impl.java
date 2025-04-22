package com.example.part2.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.part2.data.entities.Course;
import com.example.part2.data.entities.CourseWithStudents;
import com.example.part2.data.entities.Student;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CourseDao_Impl implements CourseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Course> __insertionAdapterOfCourse;

  private final EntityDeletionOrUpdateAdapter<Course> __deletionAdapterOfCourse;

  public CourseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourse = new EntityInsertionAdapter<Course>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Course` (`courseId`,`courseCode`,`courseName`,`lecturerName`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Course entity) {
        statement.bindLong(1, entity.getCourseId());
        if (entity.getCourseCode() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCourseCode());
        }
        if (entity.getCourseName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getCourseName());
        }
        if (entity.getLecturerName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLecturerName());
        }
      }
    };
    this.__deletionAdapterOfCourse = new EntityDeletionOrUpdateAdapter<Course>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Course` WHERE `courseId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Course entity) {
        statement.bindLong(1, entity.getCourseId());
      }
    };
  }

  @Override
  public void insertCourse(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCourse.insert(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCourse(final Course course) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCourse.handle(course);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<CourseWithStudents> getCoursesWithStudents() {
    final String _sql = "SELECT * FROM Course";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
        final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
        final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
        final int _cursorIndexOfLecturerName = CursorUtil.getColumnIndexOrThrow(_cursor, "lecturerName");
        final LongSparseArray<ArrayList<Student>> _collectionStudents = new LongSparseArray<ArrayList<Student>>();
        while (_cursor.moveToNext()) {
          final Long _tmpKey;
          if (_cursor.isNull(_cursorIndexOfCourseId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _cursor.getLong(_cursorIndexOfCourseId);
          }
          if (_tmpKey != null) {
            if (!_collectionStudents.containsKey(_tmpKey)) {
              _collectionStudents.put(_tmpKey, new ArrayList<Student>());
            }
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshipStudentAscomExamplePart2DataEntitiesStudent(_collectionStudents);
        final List<CourseWithStudents> _result = new ArrayList<CourseWithStudents>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final CourseWithStudents _item;
          final Course _tmpCourse;
          if (!(_cursor.isNull(_cursorIndexOfCourseId) && _cursor.isNull(_cursorIndexOfCourseCode) && _cursor.isNull(_cursorIndexOfCourseName) && _cursor.isNull(_cursorIndexOfLecturerName))) {
            final String _tmpCourseCode;
            if (_cursor.isNull(_cursorIndexOfCourseCode)) {
              _tmpCourseCode = null;
            } else {
              _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            }
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpLecturerName;
            if (_cursor.isNull(_cursorIndexOfLecturerName)) {
              _tmpLecturerName = null;
            } else {
              _tmpLecturerName = _cursor.getString(_cursorIndexOfLecturerName);
            }
            _tmpCourse = new Course(_tmpCourseCode,_tmpCourseName,_tmpLecturerName);
            final int _tmpCourseId;
            _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
            _tmpCourse.setCourseId(_tmpCourseId);
          } else {
            _tmpCourse = null;
          }
          final ArrayList<Student> _tmpStudentsCollection;
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfCourseId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfCourseId);
          }
          if (_tmpKey_1 != null) {
            _tmpStudentsCollection = _collectionStudents.get(_tmpKey_1);
          } else {
            _tmpStudentsCollection = new ArrayList<Student>();
          }
          _item = new CourseWithStudents();
          _item.course = _tmpCourse;
          _item.students = _tmpStudentsCollection;
          _result.add(_item);
        }
        __db.setTransactionSuccessful();
        return _result;
      } finally {
        _cursor.close();
        _statement.release();
      }
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Course>> getAllCoursesLive() {
    final String _sql = "SELECT * FROM Course";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"Course"}, false, new Callable<List<Course>>() {
      @Override
      @Nullable
      public List<Course> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfLecturerName = CursorUtil.getColumnIndexOrThrow(_cursor, "lecturerName");
          final List<Course> _result = new ArrayList<Course>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Course _item;
            final String _tmpCourseCode;
            if (_cursor.isNull(_cursorIndexOfCourseCode)) {
              _tmpCourseCode = null;
            } else {
              _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            }
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpLecturerName;
            if (_cursor.isNull(_cursorIndexOfLecturerName)) {
              _tmpLecturerName = null;
            } else {
              _tmpLecturerName = _cursor.getString(_cursorIndexOfLecturerName);
            }
            _item = new Course(_tmpCourseCode,_tmpCourseName,_tmpLecturerName);
            final int _tmpCourseId;
            _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
            _item.setCourseId(_tmpCourseId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<Course> getCourseByCode(final String code) {
    final String _sql = "SELECT * FROM Course WHERE courseCode = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (code == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, code);
    }
    return __db.getInvalidationTracker().createLiveData(new String[] {"Course"}, false, new Callable<Course>() {
      @Override
      @Nullable
      public Course call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
          final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
          final int _cursorIndexOfLecturerName = CursorUtil.getColumnIndexOrThrow(_cursor, "lecturerName");
          final Course _result;
          if (_cursor.moveToFirst()) {
            final String _tmpCourseCode;
            if (_cursor.isNull(_cursorIndexOfCourseCode)) {
              _tmpCourseCode = null;
            } else {
              _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
            }
            final String _tmpCourseName;
            if (_cursor.isNull(_cursorIndexOfCourseName)) {
              _tmpCourseName = null;
            } else {
              _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
            }
            final String _tmpLecturerName;
            if (_cursor.isNull(_cursorIndexOfLecturerName)) {
              _tmpLecturerName = null;
            } else {
              _tmpLecturerName = _cursor.getString(_cursorIndexOfLecturerName);
            }
            _result = new Course(_tmpCourseCode,_tmpCourseName,_tmpLecturerName);
            final int _tmpCourseId;
            _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
            _result.setCourseId(_tmpCourseId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Course getCourseByCodeSync(final String code) {
    final String _sql = "SELECT * FROM Course WHERE courseCode = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (code == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, code);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
      final int _cursorIndexOfCourseCode = CursorUtil.getColumnIndexOrThrow(_cursor, "courseCode");
      final int _cursorIndexOfCourseName = CursorUtil.getColumnIndexOrThrow(_cursor, "courseName");
      final int _cursorIndexOfLecturerName = CursorUtil.getColumnIndexOrThrow(_cursor, "lecturerName");
      final Course _result;
      if (_cursor.moveToFirst()) {
        final String _tmpCourseCode;
        if (_cursor.isNull(_cursorIndexOfCourseCode)) {
          _tmpCourseCode = null;
        } else {
          _tmpCourseCode = _cursor.getString(_cursorIndexOfCourseCode);
        }
        final String _tmpCourseName;
        if (_cursor.isNull(_cursorIndexOfCourseName)) {
          _tmpCourseName = null;
        } else {
          _tmpCourseName = _cursor.getString(_cursorIndexOfCourseName);
        }
        final String _tmpLecturerName;
        if (_cursor.isNull(_cursorIndexOfLecturerName)) {
          _tmpLecturerName = null;
        } else {
          _tmpLecturerName = _cursor.getString(_cursorIndexOfLecturerName);
        }
        _result = new Course(_tmpCourseCode,_tmpCourseName,_tmpLecturerName);
        final int _tmpCourseId;
        _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
        _result.setCourseId(_tmpCourseId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipStudentAscomExamplePart2DataEntitiesStudent(
      @NonNull final LongSparseArray<ArrayList<Student>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipStudentAscomExamplePart2DataEntitiesStudent(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `Student`.`studentId` AS `studentId`,`Student`.`name` AS `name`,`Student`.`email` AS `email`,`Student`.`userName` AS `userName`,_junction.`courseId` FROM `CourseStudentCrossRef` AS _junction INNER JOIN `Student` ON (_junction.`studentId` = `Student`.`studentId`) WHERE _junction.`courseId` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      // _junction.courseId;
      final int _itemKeyIndex = 4;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfStudentId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfEmail = 2;
      final int _cursorIndexOfUserName = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Student> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Student _item_1;
          _item_1 = new Student();
          final int _tmpStudentId;
          _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
          _item_1.setStudentId(_tmpStudentId);
          final String _tmpName;
          if (_cursor.isNull(_cursorIndexOfName)) {
            _tmpName = null;
          } else {
            _tmpName = _cursor.getString(_cursorIndexOfName);
          }
          _item_1.setName(_tmpName);
          final String _tmpEmail;
          if (_cursor.isNull(_cursorIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
          }
          _item_1.setEmail(_tmpEmail);
          final String _tmpUserName;
          if (_cursor.isNull(_cursorIndexOfUserName)) {
            _tmpUserName = null;
          } else {
            _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
          }
          _item_1.setUserName(_tmpUserName);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
