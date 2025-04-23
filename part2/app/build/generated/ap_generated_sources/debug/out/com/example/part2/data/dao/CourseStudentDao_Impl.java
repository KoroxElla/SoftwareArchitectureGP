package com.example.part2.data.dao;

import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.part2.data.entities.CourseStudentCrossRef;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CourseStudentDao_Impl implements CourseStudentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CourseStudentCrossRef> __insertionAdapterOfCourseStudentCrossRef;

  private final SharedSQLiteStatement __preparedStmtOfRemoveStudentsFromCourse;

  private final SharedSQLiteStatement __preparedStmtOfRemoveStudentFromAllCourses;

  public CourseStudentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourseStudentCrossRef = new EntityInsertionAdapter<CourseStudentCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `CourseStudentCrossRef` (`courseId`,`studentId`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final CourseStudentCrossRef entity) {
        statement.bindLong(1, entity.courseId);
        statement.bindLong(2, entity.studentId);
      }
    };
    this.__preparedStmtOfRemoveStudentsFromCourse = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM CourseStudentCrossRef WHERE courseId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfRemoveStudentFromAllCourses = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM CourseStudentCrossRef WHERE studentId = ?";
        return _query;
      }
    };
  }

  @Override
  public void enrollStudent(final CourseStudentCrossRef crossRef) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCourseStudentCrossRef.insert(crossRef);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void removeStudentsFromCourse(final int courseId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveStudentsFromCourse.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, courseId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfRemoveStudentsFromCourse.release(_stmt);
    }
  }

  @Override
  public void removeStudentFromAllCourses(final int studentId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfRemoveStudentFromAllCourses.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, studentId);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfRemoveStudentFromAllCourses.release(_stmt);
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
