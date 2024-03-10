package com.example.login.facebookdesign;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

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
        return "INSERT OR ABORT INTO `Post` (`id`,`Creator`,`PostText`,`PostLikes`,`created`,`PostImg`,`CreatorImg`,`CreatorUsername`,`liked`,`PeopleLiked`,`Comments`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCreator() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCreator());
        }
        if (entity.getPostText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPostText());
        }
        statement.bindLong(4, entity.getPostLikes());
        final Long _tmp = DateConverter.toTimestamp(entity.getCreated());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        if (entity.getPostImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPostImg());
        }
        if (entity.getCreatorImg() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCreatorImg());
        }
        if (entity.getCreatorUsername() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCreatorUsername());
        }
        final int _tmp_1 = entity.isLiked() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final String _tmp_2 = ListStringConverter.fromList(entity.getPeopleLiked());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
        final String _tmp_3 = ListStringConverter.fromList(entity.getComments());
        if (_tmp_3 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_3);
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
        return "UPDATE OR ABORT `Post` SET `id` = ?,`Creator` = ?,`PostText` = ?,`PostLikes` = ?,`created` = ?,`PostImg` = ?,`CreatorImg` = ?,`CreatorUsername` = ?,`liked` = ?,`PeopleLiked` = ?,`Comments` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final Post entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCreator() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCreator());
        }
        if (entity.getPostText() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPostText());
        }
        statement.bindLong(4, entity.getPostLikes());
        final Long _tmp = DateConverter.toTimestamp(entity.getCreated());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, _tmp);
        }
        if (entity.getPostImg() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPostImg());
        }
        if (entity.getCreatorImg() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCreatorImg());
        }
        if (entity.getCreatorUsername() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCreatorUsername());
        }
        final int _tmp_1 = entity.isLiked() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        final String _tmp_2 = ListStringConverter.fromList(entity.getPeopleLiked());
        if (_tmp_2 == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, _tmp_2);
        }
        final String _tmp_3 = ListStringConverter.fromList(entity.getComments());
        if (_tmp_3 == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, _tmp_3);
        }
        statement.bindLong(12, entity.getId());
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
      final int _cursorIndexOfCreator = CursorUtil.getColumnIndexOrThrow(_cursor, "Creator");
      final int _cursorIndexOfPostText = CursorUtil.getColumnIndexOrThrow(_cursor, "PostText");
      final int _cursorIndexOfPostLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "PostLikes");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfPostImg = CursorUtil.getColumnIndexOrThrow(_cursor, "PostImg");
      final int _cursorIndexOfCreatorImg = CursorUtil.getColumnIndexOrThrow(_cursor, "CreatorImg");
      final int _cursorIndexOfCreatorUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "CreatorUsername");
      final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
      final int _cursorIndexOfPeopleLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "PeopleLiked");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "Comments");
      final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Post _item;
        _item = new Post();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpCreator;
        if (_cursor.isNull(_cursorIndexOfCreator)) {
          _tmpCreator = null;
        } else {
          _tmpCreator = _cursor.getString(_cursorIndexOfCreator);
        }
        _item.setCreator(_tmpCreator);
        final String _tmpPostText;
        if (_cursor.isNull(_cursorIndexOfPostText)) {
          _tmpPostText = null;
        } else {
          _tmpPostText = _cursor.getString(_cursorIndexOfPostText);
        }
        _item.setPostText(_tmpPostText);
        final int _tmpPostLikes;
        _tmpPostLikes = _cursor.getInt(_cursorIndexOfPostLikes);
        _item.setPostLikes(_tmpPostLikes);
        final Date _tmpCreated;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCreated)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCreated);
        }
        _tmpCreated = DateConverter.toDate(_tmp);
        _item.setCreated(_tmpCreated);
        final String _tmpPostImg;
        if (_cursor.isNull(_cursorIndexOfPostImg)) {
          _tmpPostImg = null;
        } else {
          _tmpPostImg = _cursor.getString(_cursorIndexOfPostImg);
        }
        _item.setPostImg(_tmpPostImg);
        final String _tmpCreatorImg;
        if (_cursor.isNull(_cursorIndexOfCreatorImg)) {
          _tmpCreatorImg = null;
        } else {
          _tmpCreatorImg = _cursor.getString(_cursorIndexOfCreatorImg);
        }
        _item.setCreatorImg(_tmpCreatorImg);
        final String _tmpCreatorUsername;
        if (_cursor.isNull(_cursorIndexOfCreatorUsername)) {
          _tmpCreatorUsername = null;
        } else {
          _tmpCreatorUsername = _cursor.getString(_cursorIndexOfCreatorUsername);
        }
        _item.setCreatorUsername(_tmpCreatorUsername);
        final boolean _tmpLiked;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfLiked);
        _tmpLiked = _tmp_1 != 0;
        _item.setLiked(_tmpLiked);
        final List<String> _tmpPeopleLiked;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfPeopleLiked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfPeopleLiked);
        }
        _tmpPeopleLiked = ListStringConverter.fromString(_tmp_2);
        _item.setPeopleLiked(_tmpPeopleLiked);
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
      final int _cursorIndexOfCreator = CursorUtil.getColumnIndexOrThrow(_cursor, "Creator");
      final int _cursorIndexOfPostText = CursorUtil.getColumnIndexOrThrow(_cursor, "PostText");
      final int _cursorIndexOfPostLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "PostLikes");
      final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
      final int _cursorIndexOfPostImg = CursorUtil.getColumnIndexOrThrow(_cursor, "PostImg");
      final int _cursorIndexOfCreatorImg = CursorUtil.getColumnIndexOrThrow(_cursor, "CreatorImg");
      final int _cursorIndexOfCreatorUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "CreatorUsername");
      final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
      final int _cursorIndexOfPeopleLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "PeopleLiked");
      final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "Comments");
      final Post _result;
      if (_cursor.moveToFirst()) {
        _result = new Post();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpCreator;
        if (_cursor.isNull(_cursorIndexOfCreator)) {
          _tmpCreator = null;
        } else {
          _tmpCreator = _cursor.getString(_cursorIndexOfCreator);
        }
        _result.setCreator(_tmpCreator);
        final String _tmpPostText;
        if (_cursor.isNull(_cursorIndexOfPostText)) {
          _tmpPostText = null;
        } else {
          _tmpPostText = _cursor.getString(_cursorIndexOfPostText);
        }
        _result.setPostText(_tmpPostText);
        final int _tmpPostLikes;
        _tmpPostLikes = _cursor.getInt(_cursorIndexOfPostLikes);
        _result.setPostLikes(_tmpPostLikes);
        final Date _tmpCreated;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfCreated)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfCreated);
        }
        _tmpCreated = DateConverter.toDate(_tmp);
        _result.setCreated(_tmpCreated);
        final String _tmpPostImg;
        if (_cursor.isNull(_cursorIndexOfPostImg)) {
          _tmpPostImg = null;
        } else {
          _tmpPostImg = _cursor.getString(_cursorIndexOfPostImg);
        }
        _result.setPostImg(_tmpPostImg);
        final String _tmpCreatorImg;
        if (_cursor.isNull(_cursorIndexOfCreatorImg)) {
          _tmpCreatorImg = null;
        } else {
          _tmpCreatorImg = _cursor.getString(_cursorIndexOfCreatorImg);
        }
        _result.setCreatorImg(_tmpCreatorImg);
        final String _tmpCreatorUsername;
        if (_cursor.isNull(_cursorIndexOfCreatorUsername)) {
          _tmpCreatorUsername = null;
        } else {
          _tmpCreatorUsername = _cursor.getString(_cursorIndexOfCreatorUsername);
        }
        _result.setCreatorUsername(_tmpCreatorUsername);
        final boolean _tmpLiked;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfLiked);
        _tmpLiked = _tmp_1 != 0;
        _result.setLiked(_tmpLiked);
        final List<String> _tmpPeopleLiked;
        final String _tmp_2;
        if (_cursor.isNull(_cursorIndexOfPeopleLiked)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getString(_cursorIndexOfPeopleLiked);
        }
        _tmpPeopleLiked = ListStringConverter.fromString(_tmp_2);
        _result.setPeopleLiked(_tmpPeopleLiked);
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

  @Override
  public LiveData<List<Post>> indexLiveData() {
    final String _sql = "SELECT * FROM post";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"post"}, false, new Callable<List<Post>>() {
      @Override
      @Nullable
      public List<Post> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreator = CursorUtil.getColumnIndexOrThrow(_cursor, "Creator");
          final int _cursorIndexOfPostText = CursorUtil.getColumnIndexOrThrow(_cursor, "PostText");
          final int _cursorIndexOfPostLikes = CursorUtil.getColumnIndexOrThrow(_cursor, "PostLikes");
          final int _cursorIndexOfCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "created");
          final int _cursorIndexOfPostImg = CursorUtil.getColumnIndexOrThrow(_cursor, "PostImg");
          final int _cursorIndexOfCreatorImg = CursorUtil.getColumnIndexOrThrow(_cursor, "CreatorImg");
          final int _cursorIndexOfCreatorUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "CreatorUsername");
          final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
          final int _cursorIndexOfPeopleLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "PeopleLiked");
          final int _cursorIndexOfComments = CursorUtil.getColumnIndexOrThrow(_cursor, "Comments");
          final List<Post> _result = new ArrayList<Post>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Post _item;
            _item = new Post();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpCreator;
            if (_cursor.isNull(_cursorIndexOfCreator)) {
              _tmpCreator = null;
            } else {
              _tmpCreator = _cursor.getString(_cursorIndexOfCreator);
            }
            _item.setCreator(_tmpCreator);
            final String _tmpPostText;
            if (_cursor.isNull(_cursorIndexOfPostText)) {
              _tmpPostText = null;
            } else {
              _tmpPostText = _cursor.getString(_cursorIndexOfPostText);
            }
            _item.setPostText(_tmpPostText);
            final int _tmpPostLikes;
            _tmpPostLikes = _cursor.getInt(_cursorIndexOfPostLikes);
            _item.setPostLikes(_tmpPostLikes);
            final Date _tmpCreated;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfCreated)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfCreated);
            }
            _tmpCreated = DateConverter.toDate(_tmp);
            _item.setCreated(_tmpCreated);
            final String _tmpPostImg;
            if (_cursor.isNull(_cursorIndexOfPostImg)) {
              _tmpPostImg = null;
            } else {
              _tmpPostImg = _cursor.getString(_cursorIndexOfPostImg);
            }
            _item.setPostImg(_tmpPostImg);
            final String _tmpCreatorImg;
            if (_cursor.isNull(_cursorIndexOfCreatorImg)) {
              _tmpCreatorImg = null;
            } else {
              _tmpCreatorImg = _cursor.getString(_cursorIndexOfCreatorImg);
            }
            _item.setCreatorImg(_tmpCreatorImg);
            final String _tmpCreatorUsername;
            if (_cursor.isNull(_cursorIndexOfCreatorUsername)) {
              _tmpCreatorUsername = null;
            } else {
              _tmpCreatorUsername = _cursor.getString(_cursorIndexOfCreatorUsername);
            }
            _item.setCreatorUsername(_tmpCreatorUsername);
            final boolean _tmpLiked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfLiked);
            _tmpLiked = _tmp_1 != 0;
            _item.setLiked(_tmpLiked);
            final List<String> _tmpPeopleLiked;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfPeopleLiked)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfPeopleLiked);
            }
            _tmpPeopleLiked = ListStringConverter.fromString(_tmp_2);
            _item.setPeopleLiked(_tmpPeopleLiked);
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
