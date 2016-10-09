package net.etecassis.tcc2013.agenda.model;

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
 * Classe Modelo - Tarefa
 *
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tarefa implements Serializable {

    private int _id;
    private int tar_id;
    private String tar_titulo;
    private int tar_repetir;
    private String tar_terminoData;
    private String tar_inicioData;
    private String tar_obs;
    private String local;

    public String prepareDate(String data, String hora) {
        return data.replace("/", "") + hora.replace(":", "");
    }

    public String getFormatedDate(String date) {
        return date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 8);
    }

    public String getFormatedTime(String date) {
        return date.substring(8, 10) + ":" + date.substring(10);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getTar_id() {
        return tar_id;
    }

    public void setTar_id(int tar_id) {
        this.tar_id = tar_id;
    }

    public String getTar_titulo() {
        return tar_titulo;
    }

    public void setTar_titulo(String tar_titulo) {
        this.tar_titulo = tar_titulo;
    }

    public int getTar_repetir() {
        return tar_repetir;
    }

    public void setTar_repetir(int tar_repetir) {
        this.tar_repetir = tar_repetir;
    }

    public String getTar_terminoData() {
        return tar_terminoData;
    }

    public void setTar_terminoData(String tar_terminoData) {
        this.tar_terminoData = tar_terminoData;
    }

    public String getTar_inicioData() {
        return tar_inicioData;
    }

    public void setTar_inicioData(String tar_inicioData) {
        this.tar_inicioData = tar_inicioData;
    }

    public String getTar_obs() {
        return tar_obs;
    }

    public void setTar_obs(String tar_obs) {
        this.tar_obs = tar_obs;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String toString() {
        return this.getTar_titulo();
    }
}
