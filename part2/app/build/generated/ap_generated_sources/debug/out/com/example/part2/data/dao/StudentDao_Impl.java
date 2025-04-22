package com.example.part2.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
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
import com.example.part2.data.entities.Student;
import com.example.part2.data.entities.StudentWithCourses;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import kotlin.Unit;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class StudentDao_Impl implements StudentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Student> __insertionAdapterOfStudent;

  private final EntityDeletionOrUpdateAdapter<Student> __deletionAdapterOfStudent;

  public StudentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudent = new EntityInsertionAdapter<Student>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Student` (`studentId`,`name`,`email`,`userName`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Student entity) {
        statement.bindLong(1, entity.getStudentId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getUserName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getUserName());
        }
      }
    };
    this.__deletionAdapterOfStudent = new EntityDeletionOrUpdateAdapter<Student>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Student` WHERE `studentId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Student entity) {
        statement.bindLong(1, entity.getStudentId());
      }
    };
  }

  @Override
  public void insertStudent(final Student student) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStudent.insert(student);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteStudent(final Student student) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStudent.handle(student);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<StudentWithCourses> getStudentsWithCourses() {
    final String _sql = "SELECT * FROM Student";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
      try {
        final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "studentId");
        final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
        final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
        final int _cursorIndexOfUserName = CursorUtil.getColumnIndexOrThrow(_cursor, "userName");
        final LongSparseArray<ArrayList<Course>> _collectionCourses = new LongSparseArray<ArrayList<Course>>();
        while (_cursor.moveToNext()) {
          final Long _tmpKey;
          if (_cursor.isNull(_cursorIndexOfStudentId)) {
            _tmpKey = null;
          } else {
            _tmpKey = _cursor.getLong(_cursorIndexOfStudentId);
          }
          if (_tmpKey != null) {
            if (!_collectionCourses.containsKey(_tmpKey)) {
              _collectionCourses.put(_tmpKey, new ArrayList<Course>());
            }
          }
        }
        _cursor.moveToPosition(-1);
        __fetchRelationshipCourseAscomExamplePart2DataEntitiesCourse(_collectionCourses);
        final List<StudentWithCourses> _result = new ArrayList<StudentWithCourses>(_cursor.getCount());
        while (_cursor.moveToNext()) {
          final StudentWithCourses _item;
          final Student _tmpStudent;
          if (!(_cursor.isNull(_cursorIndexOfStudentId) && _cursor.isNull(_cursorIndexOfName) && _cursor.isNull(_cursorIndexOfEmail) && _cursor.isNull(_cursorIndexOfUserName))) {
            _tmpStudent = new Student();
            final int _tmpStudentId;
            _tmpStudentId = _cursor.getInt(_cursorIndexOfStudentId);
            _tmpStudent.setStudentId(_tmpStudentId);
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            _tmpStudent.setName(_tmpName);
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            _tmpStudent.setEmail(_tmpEmail);
            final String _tmpUserName;
            if (_cursor.isNull(_cursorIndexOfUserName)) {
              _tmpUserName = null;
            } else {
              _tmpUserName = _cursor.getString(_cursorIndexOfUserName);
            }
            _tmpStudent.setUserName(_tmpUserName);
          } else {
            _tmpStudent = null;
          }
          final ArrayList<Course> _tmpCoursesCollection;
          final Long _tmpKey_1;
          if (_cursor.isNull(_cursorIndexOfStudentId)) {
            _tmpKey_1 = null;
          } else {
            _tmpKey_1 = _cursor.getLong(_cursorIndexOfStudentId);
          }
          if (_tmpKey_1 != null) {
            _tmpCoursesCollection = _collectionCourses.get(_tmpKey_1);
          } else {
            _tmpCoursesCollection = new ArrayList<Course>();
          }
          _item = new StudentWithCourses();
          _item.student = _tmpStudent;
          _item.courses = _tmpCoursesCollection;
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipCourseAscomExamplePart2DataEntitiesCourse(
      @NonNull final LongSparseArray<ArrayList<Course>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipCourseAscomExamplePart2DataEntitiesCourse(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `Course`.`courseId` AS `courseId`,`Course`.`courseCode` AS `courseCode`,`Course`.`courseName` AS `courseName`,`Course`.`lecturerName` AS `lecturerName`,_junction.`studentId` FROM `CourseStudentCrossRef` AS _junction INNER JOIN `Course` ON (_junction.`courseId` = `Course`.`courseId`) WHERE _junction.`studentId` IN (");
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
      // _junction.studentId;
      final int _itemKeyIndex = 4;
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfCourseId = 0;
      final int _cursorIndexOfCourseCode = 1;
      final int _cursorIndexOfCourseName = 2;
      final int _cursorIndexOfLecturerName = 3;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<Course> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final Course _item_1;
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
          _item_1 = new Course(_tmpCourseCode,_tmpCourseName,_tmpLecturerName);
          final int _tmpCourseId;
          _tmpCourseId = _cursor.getInt(_cursorIndexOfCourseId);
          _item_1.setCourseId(_tmpCourseId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
