package uniandrade.br.edu.com.popseries.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import uniandrade.br.edu.com.popseries.model.Serie;

/**
 * Created by pnda on 02/04/18.
 *
 */

public class SeriesDbHelper extends SQLiteOpenHelper {
    /*===============
        DEBUG
     ================*/
    private static final String TAG = "SeriesDbHelper";

    private Context mContext;
    private long userID, serieID, usuarioSerieID;

    private static int FAVORITA = 0;
    private static int ASSISTIDA = 0;
    private static int QUERO_ASSISTIR = 0;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Series.db";

    private static final String TABLE_SERIE = "series";
    private static final String TABLE_USUARIO = "usuario";
    private static final String TABLE_USUARIO_SERIE = "usuario_serie";

    private static final String COLUMN_ID_SERIE = "_ID_SERIE";
    private static final String COLUMN_ID_USUARIO = "_ID_USUARIO";
    private static final String COLUMN_ID_USUARIO_SERIE = "_ID_USUARIO_SERIE";

    private static final String COLUMN_SERIE_ID = "serie_id";
    private static final String COLUMN_NAME_TITLE = "original_Title";
    private static final String COLUMN_NAME_OVERVIEW = "overview";
    private static final String COLUMN_NAME_API_RATE = "apiRate";

    private static final String COLUMN_NAME_USER_UID = "uid";
    private static final String COLUMN_NAME_USER_NAME = "name";
    private static final String COLUMN_NAME_USER_EMAIL = "email";

    private static final String COLUMN_NAME_FAVORITA = "favorita";
    private static final String COLUMN_NAME_ASSISTIDA = "assistida";
    private static final String COLUMN_NAME_QUERO_ASSISTIR = "quero_assistir";

    private static final String SQL_CREATE_TABLE_SERIE =
            "CREATE TABLE " + TABLE_SERIE
                    + " ("
                        + COLUMN_ID_SERIE + " INTEGER PRIMARY KEY,"
                        + COLUMN_SERIE_ID + " TEXT,"
                        + COLUMN_NAME_TITLE + " TEXT,"
                        + COLUMN_NAME_OVERVIEW + " TEXT,"
                        + COLUMN_NAME_API_RATE + " TEXT"
                    + ");";

    private static final String SQL_CREATE_TABLE_USUARIO =
            "CREATE TABLE "+ TABLE_USUARIO
                    + " ("
                        + COLUMN_ID_USUARIO + " INTEGER PRIMARY KEY,"
                        + COLUMN_NAME_USER_UID + " TEXT,"
                        + COLUMN_NAME_USER_NAME + " TEXT,"
                        + COLUMN_NAME_USER_EMAIL + " TEXT"
                    + ");";

    private static final String SQL_CREATE_TABLE_USUARIO_SERIE  =
            "CREATE TABLE "+ TABLE_USUARIO_SERIE
                    + " ("
                        + COLUMN_ID_USUARIO_SERIE + " INTEGER PRIMARY KEY,"
                        + COLUMN_ID_USUARIO + " TEXT REFERENCES " + TABLE_USUARIO + " (" + COLUMN_ID_USUARIO + "),"
                        + COLUMN_ID_SERIE + " TEXT REFERENCES " + TABLE_SERIE + " (" + COLUMN_ID_SERIE + "),"
                        + COLUMN_NAME_FAVORITA + " BOOLEAN,"
                        + COLUMN_NAME_ASSISTIDA + " BOOLEAN,"
                        + COLUMN_NAME_QUERO_ASSISTIR + " BOOLEAN"
                    + ");";

    public SeriesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_SERIE);
        db.execSQL(SQL_CREATE_TABLE_USUARIO);
        db.execSQL(SQL_CREATE_TABLE_USUARIO_SERIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    //***************************************************
    //***************************************************
    //***************************************************

    private long verificarUsuarioSerie( long user_ID, long serie_ID ) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = "SELECT _ID_USUARIO_SERIE FROM usuario_serie WHERE _ID_USUARIO = " + user_ID + " AND _ID_SERIE = " + serie_ID;

        Cursor cursor = db.rawQuery( QUERY, null );
        cursor.moveToFirst();
        if ( cursor.getCount() == 0 ) {
            db.close();
            return 0;
        }else {
            int columnIndex = cursor.getColumnIndex( COLUMN_ID_USUARIO_SERIE );
            String userIDString = cursor.getString( columnIndex );
            int usuarioSerieID = Integer.parseInt( userIDString );
            cursor.close();
            db.close();
            return usuarioSerieID;
        }
    }

    private void addUsuarioSerie(long userID, long newRowSerieId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID_USUARIO, userID);
        values.put(COLUMN_ID_SERIE, newRowSerieId);
        values.put(COLUMN_NAME_FAVORITA, FAVORITA);
        values.put(COLUMN_NAME_ASSISTIDA, ASSISTIDA);
        values.put(COLUMN_NAME_QUERO_ASSISTIR, QUERO_ASSISTIR);

        db.insert(TABLE_USUARIO_SERIE, null, values);
        db.close();
    }

    private void updateUsuarioSerie(long usuarioSerieID, String column) {
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY_UPDATE = "UPDATE usuario_serie SET " + column + " = 1 WHERE _ID_USUARIO_SERIE = " + usuarioSerieID;
        db.execSQL(QUERY_UPDATE);
        db.close();
    }

    private long verificarUsuarioBanco() {
        Preferencias preferencias = new Preferencias( mContext );
        SQLiteDatabase db = this.getWritableDatabase();
        String identificadorUsuario = preferencias.getIdentificador();

        String QUERY = "SELECT _ID_USUARIO  FROM usuario WHERE uid = \'" +identificadorUsuario+"\'";

        Cursor cursor = db.rawQuery( QUERY, null );
        cursor.moveToFirst();
        if ( cursor.getCount() == 0 ) {
            db.close();
            return 0;
        }else {
            int columnIndex = cursor.getColumnIndex(COLUMN_ID_USUARIO);
            String userIDString = cursor.getString(columnIndex);
            int userID = Integer.parseInt(userIDString);
            cursor.close();
            db.close();
            return userID;
        }
    }

    private long addUserBanco() {
        Preferencias preferencias = new Preferencias( mContext );
        SQLiteDatabase db = this.getWritableDatabase();
        String identificadorUsuario = preferencias.getIdentificador();
        String userName = preferencias.getNome();
        String userEmail = preferencias.getEmail();

        ContentValues values = new ContentValues();

        values.put( COLUMN_NAME_USER_UID, identificadorUsuario );
        values.put( COLUMN_NAME_USER_NAME, userName );
        values.put( COLUMN_NAME_USER_EMAIL, userEmail );

        long newRowUSerId = db.insert(TABLE_USUARIO, null, values);
        db.close();

        return newRowUSerId;
    }

    private long verificarSerieBanco(int serie_id){
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY = "SELECT _ID_SERIE  FROM series WHERE serie_id = " + serie_id;

        Cursor cursor = db.rawQuery( QUERY, null );
        cursor.moveToFirst();
        if ( cursor.getCount() == 0 ) {
            db.close();
            return 0;
        }else {
            int columnIndex = cursor.getColumnIndex( COLUMN_ID_SERIE );
            String userIDString = cursor.getString( columnIndex );
            int serieID = Integer.parseInt( userIDString );
            cursor.close();
            db.close();
            return serieID;
        }
    }

    private long addSerieBanco(Serie serie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put( COLUMN_SERIE_ID, serie.getSerie_ID() );
        values.put( COLUMN_NAME_TITLE, serie.getOriginal_Title() );
        values.put( COLUMN_NAME_OVERVIEW, serie.getOverview() );
        values.put( COLUMN_NAME_API_RATE, serie.getApiRate() );

        long newRowSerieId = db.insert(TABLE_SERIE, null, values);
        db.close();
        return newRowSerieId;
    }

    public void addFavorito(Serie serie){
        FAVORITA = 1;
        String column = "favorita";
        userID = verificarUsuarioBanco();
        serieID = verificarSerieBanco( serie.getSerie_ID() );
        usuarioSerieID = verificarUsuarioSerie( userID, serieID );

        if ( userID == 0 ){
            userID = addUserBanco();
        }
        if ( serieID == 0 ){
            serieID = addSerieBanco( serie );
        }
        if (usuarioSerieID == 0) {
            addUsuarioSerie( userID, serieID );
        }else {
            updateUsuarioSerie( usuarioSerieID, column );
        }
        FAVORITA = 0;
    }

    public void addAssistida(Serie serie) {
        ASSISTIDA = 1;
        String column = "assistida";
        userID = verificarUsuarioBanco();
        serieID = verificarSerieBanco( serie.getSerie_ID() );
        usuarioSerieID = verificarUsuarioSerie( userID, serieID );

        if ( userID == 0 ){
            userID = addUserBanco();
        }
        if ( serieID == 0 ){
            serieID = addSerieBanco(serie);
        }
        if (usuarioSerieID == 0) {
            addUsuarioSerie( userID, serieID );
        }else {
            updateUsuarioSerie( usuarioSerieID, column );
        }
        ASSISTIDA = 0;
    }

    public void addQueroAssistir( Serie serie ){
        QUERO_ASSISTIR = 1;
        String column = "quero_assistir";
        userID = verificarUsuarioBanco();
        serieID = verificarSerieBanco( serie.getSerie_ID() );
        usuarioSerieID = verificarUsuarioSerie( userID, serieID );

        if ( userID == 0 ){
            userID = addUserBanco();
        }
        if ( serieID == 0 ){
            serieID = addSerieBanco(serie);
        }
        if (usuarioSerieID == 0) {
            addUsuarioSerie( userID, serieID );
        }else {
            updateUsuarioSerie( usuarioSerieID, column );
        }
        QUERO_ASSISTIR = 0;
    }

    public void removerFavorito(String column, int serie_id) {
        userID = verificarUsuarioBanco();
        serieID = verificarSerieBanco( serie_id );
        usuarioSerieID = verificarUsuarioSerie( userID, serieID );
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY_UPDATE = "UPDATE usuario_serie SET " + column + " = 0 WHERE _ID_USUARIO_SERIE = " + usuarioSerieID;
        db.execSQL(QUERY_UPDATE);
        db.close();
    }

    public void removerAssistido(String column, int serie_id) {
        userID = verificarUsuarioBanco();
        serieID = verificarSerieBanco( serie_id );
        usuarioSerieID = verificarUsuarioSerie( userID, serieID );
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY_UPDATE = "UPDATE usuario_serie SET " + column + " = 0 WHERE _ID_USUARIO_SERIE = " + usuarioSerieID;
        db.execSQL(QUERY_UPDATE);
        db.close();
    }

    public void removerQueroAssistir(String column, int serie_id) {
        userID = verificarUsuarioBanco();
        serieID = verificarSerieBanco( serie_id );
        usuarioSerieID = verificarUsuarioSerie( userID, serieID );
        SQLiteDatabase db = this.getWritableDatabase();

        String QUERY_UPDATE = "UPDATE usuario_serie SET " + column + " = 0 WHERE _ID_USUARIO_SERIE = " + usuarioSerieID;
        db.execSQL(QUERY_UPDATE);
        db.close();
    }

    public boolean verificaFavorito(int serie_id ){
        long user_ID = verificarUsuarioBanco();
        long serie_ID = verificarSerieBanco( serie_id );
        String QUERY = "SELECT favorita FROM usuario_serie WHERE _ID_USUARIO = " + user_ID + " AND _ID_SERIE = " + serie_ID + " AND favorita = 1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( QUERY, null );
        cursor.moveToFirst();
        if ( cursor.getCount() == 0 ) {
            db.close();
            return false;
        }else {
            cursor.close();
            db.close();
            return true;
        }
    }

    public boolean verificaAssistido(int serie_id ){
        long user_ID = verificarUsuarioBanco();
        long serie_ID = verificarSerieBanco( serie_id );
        String QUERY = "SELECT assistida FROM usuario_serie WHERE _ID_USUARIO = " + user_ID + " AND _ID_SERIE = " + serie_ID + " AND assistida = 1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( QUERY, null );
        cursor.moveToFirst();
        if ( cursor.getCount() == 0 ) {
            db.close();
            return false;
        }else {
            cursor.close();
            db.close();
            return true;
        }
    }

    public boolean verificaQueroAssistir(int serie_id ){
        long user_ID = verificarUsuarioBanco();
        long serie_ID = verificarSerieBanco( serie_id );
        String QUERY = "SELECT quero_assistir FROM usuario_serie WHERE _ID_USUARIO = " + user_ID + " AND _ID_SERIE = " + serie_ID + " AND quero_assistir = 1 ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( QUERY, null );
        cursor.moveToFirst();
        if ( cursor.getCount() == 0 ) {
            db.close();
            return false;
        }else {
            cursor.close();
            db.close();
            return true;
        }
    }

    public void retornaFavoritos(){
        try {
            Preferencias preferencias = new Preferencias( mContext );
            SQLiteDatabase db = this.getWritableDatabase();
            String identificadorUsuario = preferencias.getIdentificador();

            String QUERY =
                    "SELECT *  " +
                    "FROM " + TABLE_USUARIO_SERIE;
            Cursor cursor = db.rawQuery( QUERY, null );

            int indiceID = cursor.getColumnIndex(COLUMN_ID_USUARIO_SERIE);
            int indiceIDUsuario = cursor.getColumnIndex(COLUMN_ID_USUARIO);
            int indiceIDSerie = cursor.getColumnIndex(COLUMN_ID_SERIE);
            int indiceFavorita = cursor.getColumnIndex(COLUMN_NAME_FAVORITA);
            int indiceAssistida = cursor.getColumnIndex(COLUMN_NAME_ASSISTIDA);
            int indiceQueroAssistir = cursor.getColumnIndex(COLUMN_NAME_QUERO_ASSISTIR);

            cursor.moveToFirst();
            while (cursor != null){
                Log.i("RESULT ID_USUARIO: ", cursor.getString(indiceIDUsuario) );
                Log.i("RESULT ID_SERIE: ", cursor.getString(indiceIDSerie) );
                Log.i("RESULT FAVORITA: ", cursor.getString(indiceFavorita) );
                Log.i("RESULT ASSISTIDA: ", cursor.getString(indiceAssistida) );
                Log.i("RESULT QUERO_ASSISTIR: ", cursor.getString(indiceQueroAssistir) );
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
