package com.example.login.facebookdesign;

import android.database.Cursor;
import android.graphics.Bitmap;
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
public final class PostDao_Impl implements PostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Post> __insertionAdapterOfPost;

  private final EntityDeletionOrUpdateAdapter<Post> __deletionAdapterOfPost;

  private final EntityDeletionOrUpdateAdapter<Post> __updateAdapterOfPost;

  private final SharedSQLiteStatement __preparedStmtOfClear;

  public PostDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPost = new EntityInsertionAdapter<Post>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Post` (`id`,`author`,`content`,`likes`,`pic`,`profilepic`,`liked`,`timestamp`,`comments`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAuthor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAuthor());
        }
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        statement.bindLong(4, entity.getLikes());
        final byte[] _tmp = BitmapConverter.fromBitmap(entity.getPic());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindBlob(5, _tmp);
        }
        final byte[] _tmp_1 = BitmapConverter.fromBitmap(entity.getProfilepic());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindBlob(6, _tmp_1);
        }
        final int _tmp_2 = entity.isLiked() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        statement.bindLong(8, entity.getTimestamp());
        final String _tmp_3 = ListStringConverter.fromList(entity.getComments());
        if (_tmp_3 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_3);
        }
      }
    };
    this.__deletionAdapterOfPost = new EntityDeletionOrUpdateAdapter<Post>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Post` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfPost = new EntityDeletionOrUpdateAdapter<Post>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Post` SET `id` = ?,`author` = ?,`content` = ?,`likes` = ?,`pic` = ?,`profilepic` = ?,`liked` = ?,`timestamp` = ?,`comments` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getAuthor() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getAuthor());
        }
        if (entity.getContent() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getContent());
        }
        statement.bindLong(4, entity.getLikes());
        final byte[] _tmp = BitmapConverter.fromBitmap(entity.getPic());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindBlob(5, _tmp);
        }
        final byte[] _tmp_1 = BitmapConverter.fromBitmap(entity.getProfilepic());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindBlob(6, _tmp_1);
        }
        final int _tmp_2 = entity.isLiked() ? 1 : 0;
        statement.bindLong(7, _tmp_2);
        statement.bindLong(8, entity.getTimestamp());
        final String _tmp_3 = ListStringConverter.fromList(entity.getComments());
        if (_tmp_3 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_3);
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfClear = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM post";
        return _query;
      }
    };
  }

  @Override
  public void insertList(final List<Post> posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPost.insert(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insert(final Post... posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfPost.insert(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Post... posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfPost.handleMultiple(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Post... posts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfPost.handleMultiple(posts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void clear() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfClear.acquire();
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfClear.release(_stmt);
    }
  }

  @Override
  public List<Post> index() {
    final String _sql = "SELECT * FROM post";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final int _cursorIndexOfPic = CursorUtil.getColumnIndexOrThrow(_cursor, "pic");
      final int _cursorIndexOfProfilepic = CursorUtil.getColumnIndexOrThrow(_cursor, "profilepic");
      final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Post _item;
        _item = new Post();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpAuthor;
        if (_cursor.isNull(_cursorIndexOfAuthor)) {
          _tmpAuthor = null;
        } else {
          _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        }
        _item.setAuthor(_tmpAuthor);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _item.setContent(_tmpContent);
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        _item.setLikes(_tmpLikes);
        final Bitmap _tmpPic;
        final byte[] _tmp;
        if (_cursor.isNull(_cursorIndexOfPic)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getBlob(_cursorIndexOfPic);
        }
        _tmpPic = BitmapConverter.toBitmap(_tmp);
        _item.setPic(_tmpPic);
        final Bitmap _tmpProfilepic;
        final byte[] _tmp_1;
        if (_cursor.isNull(_cursorIndexOfProfilepic)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getBlob(_cursorIndexOfProfilepic);
        }
        _tmpProfilepic = BitmapConverter.toBitmap(_tmp_1);
        _item.setProfilepic(_tmpProfilepic);
        final boolean _tmpLiked;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfLiked);
        _tmpLiked = _tmp_2 != 0;
        _item.setLiked(_tmpLiked);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _item.setTimestamp(_tmpTimestamp);
        final List<String> _tmpComments;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfComments);
        }
        _tmpComments = ListStringConverter.fromString(_tmp_3);
        _item.setComments(_tmpComments);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Post get(final int id) {
    final String _sql = "SELECT * FROM post WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final int _cursorIndexOfLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "likes");
      final int _cursorIndexOfPic = CursorUtil.getColumnIndexOrThrow(_cursor, "pic");
      final int _cursorIndexOfProfilepic = CursorUtil.getColumnIndexOrThrow(_cursor, "profilepic");
      final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
      final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "comments");
      final Post _result;
      if (_cursor.moveToFirst()) {
        _result = new Post();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpAuthor;
        if (_cursor.isNull(_cursorIndexOfAuthor)) {
          _tmpAuthor = null;
        } else {
          _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
        }
        _result.setAuthor(_tmpAuthor);
        final String _tmpContent;
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _tmpContent = null;
        } else {
          _tmpContent = _cursor.getString(_cursorIndexOfContent);
        }
        _result.setContent(_tmpContent);
        final int _tmpLikes;
        _tmpLikes = _cursor.getInt(_cursorIndexOfLikes);
        _result.setLikes(_tmpLikes);
        final Bitmap _tmpPic;
        final byte[] _tmp;
        if (_cursor.isNull(_cursorIndexOfPic)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getBlob(_cursorIndexOfPic);
        }
        _tmpPic = BitmapConverter.toBitmap(_tmp);
        _result.setPic(_tmpPic);
        final Bitmap _tmpProfilepic;
        final byte[] _tmp_1;
        if (_cursor.isNull(_cursorIndexOfProfilepic)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getBlob(_cursorIndexOfProfilepic);
        }
        _tmpProfilepic = BitmapConverter.toBitmap(_tmp_1);
        _result.setProfilepic(_tmpProfilepic);
        final boolean _tmpLiked;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfLiked);
        _tmpLiked = _tmp_2 != 0;
        _result.setLiked(_tmpLiked);
        final long _tmpTimestamp;
        _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
        _result.setTimestamp(_tmpTimestamp);
        final List<String> _tmpComments;
        final String _tmp_3;
        if (_cursor.isNull(_cursorIndexOfComments)) {
          _tmp_3 = null;
        } else {
          _tmp_3 = _cursor.getString(_cursorIndexOfComments);
        }
        _tmpComments = ListStringConverter.fromString(_tmp_3);
        _result.setComments(_tmpComments);
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
