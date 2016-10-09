package net.etecassis.tcc2013.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/* Trabalho de Conclusão de Curso
 * Agenda para Android
 *
 * Desenvolvedores: Vitor Oliveira, Leonardo Martins, Weveston Rezende e Ricardo Alvim
 * Agradecimentos ao Desenvolvedor e Professor Marco Carvalho
 *
 * Etec Pedro D'Arcádia Neto
 * Técnico em Informática
 * Turma do 1º Semestre de 2012
 *
 *
 * ricardoalvim.comoj.com/agenda
 *
 * Classe de Banco de Dados
 *
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String BANCO_DADOS = "Agenda";
    private static int VERSAO = 1;

    public static class Tarefas {
        public static final String TABELA = "tarefas";
        public static final String _ID = "_id";
        public static final String TAR_TITULO = "tar_titulo";
        public static final String TAR_TERMINODATA = "tar_terminoData";
        public static final String TAR_INICIODATA = "tar_inicioData";
        public static final String TAR_OBS = "tar_obs";
        public static final String LOCAL = "local";

        public static final String[] COLUNAS = new String[]{
                _ID, TABELA, TAR_TITULO, TAR_TERMINODATA, TAR_INICIODATA, TAR_OBS, LOCAL};
    }

    public DatabaseHelper(Context context) {
        super(context, BANCO_DADOS, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tarefas (" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," + //0
                " not_id INTEGER," + //1
                " tar_titulo TEXT, " + //2
                " tar_repetir INT, " + //3
                " tar_terminoData TEXT, " + //4
                " tar_inicioData TEXT, " + //5
                " tar_obs TEXT, " + //6
                " local TEXT" + //7
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
