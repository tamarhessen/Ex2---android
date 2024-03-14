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
public final class CommentPostDao_Impl implements CommentPostDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CommentPost> __insertionAdapterOfCommentPost;

  private final CommentConventor __commentConventor = new CommentConventor();

  private final EntityDeletionOrUpdateAdapter<CommentPost> __updateAdapterOfCommentPost;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public CommentPostDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCommentPost = new EntityInsertionAdapter<CommentPost>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `commentPost` (`postId`,`listComment`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final CommentPost entity) {
        statement.bindLong(1, entity.getPostId());
        final String _tmp = __commentConventor.fromCommentList(entity.getListComment());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
      }
    };
    this.__updateAdapterOfCommentPost = new EntityDeletionOrUpdateAdapter<CommentPost>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `commentPost` SET `postId` = ?,`listComment` = ? WHERE `postId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final CommentPost entity) {
        statement.bindLong(1, entity.getPostId());
        final String _tmp = __commentConventor.fromCommentList(entity.getListComment());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindLong(3, entity.getPostId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM commentPost";
        return _query;
      }
    };
  }

  @Override
  public void insert(final CommentPost... commentPosts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCommentPost.insert(commentPosts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final CommentPost... commentPosts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCommentPost.handleMultiple(commentPosts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final CommentPost... commentPosts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCommentPost.handleMultiple(commentPosts);
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
  public List<CommentPost> index() {
    final String _sql = "SELECT * FROM commentPost";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPostId = CursorUtil.getColumnIndexOrThrow(_cursor, "postId");
      final int _cursorIndexOfListComment = CursorUtil.getColumnIndexOrThrow(_cursor, "listComment");
      final List<CommentPost> _result = new ArrayList<CommentPost>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final CommentPost _item;
        final int _tmpPostId;
        _tmpPostId = _cursor.getInt(_cursorIndexOfPostId);
        _item = new CommentPost(_tmpPostId);
        final List<Comment> _tmpListComment;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfListComment)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfListComment);
        }
        _tmpListComment = __commentConventor.toCommentList(_tmp);
        _item.setListComment(_tmpListComment);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public CommentPost get(final int id) {
    final String _sql = "SELECT * FROM commentPost WHERE postId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPostId = CursorUtil.getColumnIndexOrThrow(_cursor, "postId");
      final int _cursorIndexOfListComment = CursorUtil.getColumnIndexOrThrow(_cursor, "listComment");
      final CommentPost _result;
      if (_cursor.moveToFirst()) {
        final int _tmpPostId;
        _tmpPostId = _cursor.getInt(_cursorIndexOfPostId);
        _result = new CommentPost(_tmpPostId);
        final List<Comment> _tmpListComment;
        final String _tmp;
        if (_cursor.isNull(_cursorIndexOfListComment)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getString(_cursorIndexOfListComment);
        }
        _tmpListComment = __commentConventor.toCommentList(_tmp);
        _result.setListComment(_tmpListComment);
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
