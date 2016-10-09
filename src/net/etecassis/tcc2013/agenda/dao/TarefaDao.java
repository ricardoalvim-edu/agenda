package net.etecassis.tcc2013.agenda.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.etecassis.tcc2013.agenda.DatabaseHelper;
import net.etecassis.tcc2013.agenda.model.Tarefa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Classe do Data Access Object - Tarefa
 *
 */


public class TarefaDao {
    private DatabaseHelper helper;

    public enum SELECT_BY {ALL, DAY, WEEK, MONTH}

    ;

    //
    public TarefaDao(Context context) {
        helper = new DatabaseHelper(context);
    }

    //Salvar tarefa
    public void insertTarefa(Tarefa tarefa) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        ctv.put("tar_titulo", tarefa.getTar_titulo());
        ctv.put("tar_obs", tarefa.getTar_obs());
        ctv.put("local", tarefa.getLocal());
        ctv.put("tar_inicioData", tarefa.getTar_inicioData());
        ctv.put("tar_terminoData", tarefa.getTar_terminoData());
        db.insert("tarefas", "_id", ctv);
        db.close();
    }

    //Atualizar tarefa
    public int updateTarefa(Tarefa tarefa) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues ctv = new ContentValues();
        ctv.put("tar_titulo", tarefa.getTar_titulo());
        ctv.put("tar_obs", tarefa.getTar_obs());
        ctv.put("local", tarefa.getLocal());
        ctv.put("tar_inicioData", tarefa.getTar_inicioData());
        ctv.put("tar_terminoData", tarefa.getTar_terminoData());
        int ret = db.update("tarefas", ctv, "_id = ?", new String[]{Integer.toString((tarefa.get_id()))});
        return ret;
    }

    //Remover tarefa
    public int deleteTarefa(Tarefa tarefa) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = db.delete("tarefas", "_id = ?", new String[]{Integer.toString((tarefa.get_id()))});
        db.close();
        return ret;
    }


    public List<String> listStringTarefas() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tar_titulo FROM tarefas", null);
        cursor.moveToFirst();
        List<String> tarefas = new ArrayList<String>();
        for (int i = 0; i < cursor.getCount(); i++) {
            tarefas.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return tarefas;
    }

    //Hash
    public List<Map<String, Tarefa>> listTarefas() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tarefas", null);
        cursor.moveToFirst();

        List<Map<String, Tarefa>> tarefas = new ArrayList<Map<String, Tarefa>>();

        for (int i = 0; i < cursor.getCount(); i++) {
            Map<String, Tarefa> item = new HashMap<String, Tarefa>();
            //Tarefa item = new Tarefa();
            Tarefa tarefa = new Tarefa();
            tarefa.set_id(cursor.getInt(0));
            tarefa.setTar_titulo(cursor.getString(2));
            tarefa.setTar_repetir(cursor.getInt(3));
            tarefa.setTar_terminoData(cursor.getString(4));
            tarefa.setTar_inicioData(cursor.getString(5));
            tarefa.setTar_obs(cursor.getString(6));
            tarefa.setLocal(cursor.getString(7));
            item.put(tarefa.getTar_titulo(), tarefa);
            tarefas.add(item);
            cursor.moveToNext();
        }
        return tarefas;
    }

    //Método para pesquisa por título da tarefa
    public List<Tarefa> listarTarefas(String query) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db
                .rawQuery("SELECT * FROM tarefas WHERE tar_titulo LIKE '%"
                        + query + "%'", null);
        cursor.moveToFirst();
        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Tarefa tarefa = new Tarefa();
            tarefa.set_id(cursor.getInt(0));
            tarefa.setTar_titulo(cursor.getString(2));
            tarefa.setTar_repetir(cursor.getInt(3));
            tarefa.setTar_terminoData(cursor.getString(4));
            tarefa.setTar_inicioData(cursor.getString(5));
            tarefa.setTar_obs(cursor.getString(6));
            tarefa.setLocal(cursor.getString(7));
            tarefas.add(tarefa);
            cursor.moveToNext();
        }
        return tarefas;
    }

    //Novo método de procura por enumeração
    public List<Tarefa> listTarefas(TarefaDao.SELECT_BY selecao) {
        String query = "SELECT * FROM tarefas";
        SQLiteDatabase db = helper.getReadableDatabase();

        if (selecao == TarefaDao.SELECT_BY.ALL) {
            query = "SELECT * FROM tarefas";
            db = helper.getReadableDatabase();
        }

        if (selecao == TarefaDao.SELECT_BY.DAY) {
            Calendar calendar = Calendar.getInstance();

            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH) + 1;
            System.out.println("Date: " + mes);
            int ano = calendar.get(Calendar.YEAR);
            String dateQuery = dia + "" + mes + "" + ano;
            ;

            if (mes < 10 && dia < 10) {
                dateQuery = "0" + dia + "0" + mes + "" + ano;
            } else if (mes < 10 && dia > 10) {
                dateQuery = dia + "0" + mes + "" + ano;
            }

            query = "SELECT * FROM tarefas WHERE tar_inicioData BETWEEN '" + dateQuery + "0000' AND '" + dateQuery + "2359'";
            System.out.println("Query: " + query);
            db = helper.getReadableDatabase();
        }

        if (selecao == TarefaDao.SELECT_BY.WEEK) {
            Calendar calendar = Calendar.getInstance();

            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH) + 1;
            int ano = calendar.get(Calendar.YEAR);
            String dateQuery = "";
            String dateQueryFinal = "";

            if (dia < 10 && mes < 10) {
                dateQuery = "0" + dia + "0" + mes + ano;
                dateQueryFinal = "0" + (dia + 6) + "0" + mes + ano;
            }
            if (dia > 10 && mes < 10) {
                dateQuery = dia + "0" + mes + "" + ano;
                dateQueryFinal = (dia + 6) + "0" + mes + "" + ano;
            }
            if (dia + 7 > 29 && mes == 2) {
                dateQuery = dia + "" + mes + "" + ano;
                dateQueryFinal = "0" + ((dia + 6) - 29) + "0" + (mes + 1) + "" + ano;

            }
            if (dia + 7 > 30 && mes < 10) {
                dateQuery = dia + "" + mes + "" + ano;
                dateQueryFinal = "0" + ((dia + 6) - 30) + "0" + (mes + 1) + "" + ano;
            }
            if (dia + 7 > 30 && mes > 10) {
                dateQuery = dia + "" + mes + "" + ano;
                dateQueryFinal = "0" + ((dia + 6) - 30) + "" + (mes + 1) + "" + ano;
            }

            query = "SELECT * FROM tarefas WHERE tar_inicioData BETWEEN '" + dateQuery + "0000' AND '" + dateQueryFinal + "2359'";
            System.out.println("Query: " + query);
            db = helper.getReadableDatabase();
        }

        if (selecao == TarefaDao.SELECT_BY.MONTH) {
            Calendar calendar = Calendar.getInstance();

            int dia = calendar.get(Calendar.DAY_OF_MONTH);
            int mes = calendar.get(Calendar.MONTH) + 1;
            int ano = calendar.get(Calendar.YEAR);
            String dateQuery = "";
            String dateQueryFinal = "";

            if (dia < 10 && mes < 10) {
                dateQuery = "0" + dia + "0" + mes + ano;
                dateQueryFinal = "0" + dia + "0" + (mes + 1) + ano;
            }

            if (mes < 10 && dia > 10) {
                dateQuery = dia + "0" + mes + "" + ano;
                dateQueryFinal = dia + "0" + (mes + 1) + "" + ano;
            }
            if (mes >= 12) {
                dateQuery = dia + "0" + mes + "" + ano;
                dateQueryFinal = dia + "" + mes + "" + (ano + 1);
            }

            query = "SELECT * FROM tarefas WHERE tar_inicioData BETWEEN '" + dateQuery + "0000' AND '" + dateQueryFinal + "2359'";
            System.out.println("Query: " + query);
            db = helper.getReadableDatabase();

        }

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        List<Tarefa> tarefas = new ArrayList<Tarefa>();
        for (int i = 0; i < cursor.getCount(); i++) {
            Tarefa tarefa = new Tarefa();
            tarefa.set_id(cursor.getInt(0));
            tarefa.setTar_titulo(cursor.getString(2));
            tarefa.setTar_repetir(cursor.getInt(3));
            tarefa.setTar_terminoData(cursor.getString(4));
            tarefa.setTar_inicioData(cursor.getString(5));
            tarefa.setTar_obs(cursor.getString(6));
            tarefa.setLocal(cursor.getString(7));
            tarefas.add(tarefa);
            cursor.moveToNext();
        }
        return tarefas;
    }

}
