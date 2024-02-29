package com.example.login.facebookdesign;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `user` (`id`,`username`,`user`,`lastComment`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUsername());
        }
        final String _tmp = UserNoPasswordConverter.toString(entity.getUser());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = CommentDetailsConventor.toString(entity.lastComment);
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `user` SET `id` = ?,`username` = ?,`user` = ?,`lastComment` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getUsername() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getUsername());
        }
        final String _tmp = UserNoPasswordConverter.toString(entity.getUser());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        final String _tmp_1 = CommentDetailsConventor.toString(entity.lastComment);
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        statement.bindLong(5, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM user";
        return _query;
      }
    };
  }

  @Override
  public void insert(final User... users) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(users);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final User... users) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUser.handleMultiple(users);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<User> index() {
    final String _sql = "SELECT * FROM user";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfUser = CursorUtil.getColumnIndexOrThrow(_cursor, "user");
      final int _cursorIndexOfLastComment = CursorUtil.getColumnIndexOrThrow(_cursor, "lastComment");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final User _item;
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        final User.UserNoPassword _tmpUser;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfUser)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfUser);
        }
        _tmpUser = UserNoPasswordConverter.fromString(_tmp);
        final User.CommentDetails _tmpLastComment;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfLastComment)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfLastComment);
        }
        _tmpLastComment = CommentDetailsConventor.fromString(_tmp_1);
        _item = new User(_tmpId,_tmpUsername,_tmpUser,_tmpLastComment);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public User get(final int id) {
    final String _sql = "SELECT * FROM user WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfUser = CursorUtil.getColumnIndexOrThrow(_cursor, "user");
      final int _cursorIndexOfLastComment = CursorUtil.getColumnIndexOrThrow(_cursor, "lastComment");
      final User _result;
      if (_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpUsername;
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _tmpUsername = null;
        } else {
          _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        }
        final User.UserNoPassword _tmpUser;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfUser)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfUser);
        }
        _tmpUser = UserNoPasswordConverter.fromString(_tmp);
        final User.CommentDetails _tmpLastComment;
        final String _tmp_1;
        if (_cursor.isNull(_cursorIndexOfLastComment)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getString(_cursorIndexOfLastComment);
        }
        _tmpLastComment = CommentDetailsConventor.fromString(_tmp_1);
        _result = new User(_tmpId,_tmpUsername,_tmpUser,_tmpLastComment);
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
}
