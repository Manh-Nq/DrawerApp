package com.example.testprogram.contentprovider


import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.R)
class StudentProvider : ContentProvider() {

    private val TAG = StudentProvider::class.java.simpleName
    private lateinit var studentDb: StudentDatabase

    override fun onCreate(): Boolean {
        studentDb = StudentDatabase(requireContext())
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: uri=$uri , projection= $projection, selection= $selection, selectionArgs= $selectionArgs, sortOrder= $sortOrder")
        val db = studentDb.readableDatabase
        return when (uriMatcher.match(uri)) {
            STUDENTS -> db.query(
                StudentDatabase.TABLE_STUDENTS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
            )
            STUDENT_ID -> {
                val id = ContentUris.parseId(uri)
                db.query(
                    StudentDatabase.TABLE_STUDENTS,
                    projection,
                    "${StudentDatabase.COLUMN_ID}=?",
                    arrayOf(id.toString()),
                    null,
                    null,
                    sortOrder
                )
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun getType(uri: Uri): String {
        Log.d(TAG, "getType: uri=$uri")
        return when (uriMatcher.match(uri)) {
            STUDENTS -> "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$AUTHORITY.$TABLE_NAME"
            STUDENT_ID -> "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/$AUTHORITY.$TABLE_NAME"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.d(TAG, "insert: uri= $uri, value= $values")
        Log.d(TAG, "insert: matchedCode: ${uriMatcher.match(uri)} , STUDENTS_code= $STUDENTS, STUDENT_ID code = $STUDENT_ID")
        val db = studentDb.writableDatabase
        val id = when (uriMatcher.match(uri)) {
            STUDENTS -> db.insert(StudentDatabase.TABLE_STUDENTS, null, values)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        return if (id > 0) {
            ContentUris.withAppendedId(CONTENT_URI, id)
        } else {
            throw IllegalArgumentException("Failed to insert row into $values")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.d(TAG, "delete: uri= $uri, selection= $selection, selectionArgs= $selectionArgs")
        val db = studentDb.writableDatabase
        return when (uriMatcher.match(uri)) {
            STUDENTS -> db.delete(StudentDatabase.TABLE_STUDENTS, selection, selectionArgs)
            STUDENT_ID -> {
                val id = ContentUris.parseId(uri)
                db.delete(
                    StudentDatabase.TABLE_STUDENTS,
                    "${StudentDatabase.COLUMN_ID}=?",
                    arrayOf(id.toString())
                )
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        Log.d(TAG, "update: uri= $uri, selection= $selection, selectionArgs= $selectionArgs")
        val db = studentDb.writableDatabase
        return when (uriMatcher.match(uri)) {
            STUDENTS -> db.update(StudentDatabase.TABLE_STUDENTS, values, selection, selectionArgs)
            STUDENT_ID -> {
                val id = ContentUris.parseId(uri)
                db.update(
                    StudentDatabase.TABLE_STUDENTS,
                    values,
                    "${StudentDatabase.COLUMN_ID}=?",
                    arrayOf(id.toString())
                )
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    companion object {
        const val AUTHORITY = "com.example.apilessonapp"
        const val TABLE_NAME = "students"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
        const val STUDENTS = 1
        const val STUDENT_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, TABLE_NAME, STUDENTS)
            addURI(AUTHORITY, "$TABLE_NAME/#", STUDENT_ID)
        }
    }
}
