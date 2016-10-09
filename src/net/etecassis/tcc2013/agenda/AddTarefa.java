package net.etecassis.tcc2013.agenda;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.etecassis.tcc2013.agenda.dao.TarefaDao;
import net.etecassis.tcc2013.agenda.model.Tarefa;

import java.util.Calendar;

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
 * Classe da atvidade Adicionar Tarefa - Create
 *
 */

public class AddTarefa extends Activity {
    private int ano, mes, dia, hora, minuto;

    private EditText etTitulo;
    private EditText etObs;
    private EditText etLocal;
    private EditText dataInicio;
    private EditText dataTermino;
    private EditText horaInicio;
    private EditText horaTermino;

    private String prepareDatePart(String part) {
        return (part.length() == 1) ? 0 + part : part;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        // Prepara a actionbar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Adicionar Tarefa");
        actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Pega a data e hora atual do sistema
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        hora = calendar.get(Calendar.HOUR_OF_DAY);
        minuto = calendar.get(Calendar.MINUTE);

        // Vincula os atributos da classe aos controles do layout
        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etObs = (EditText) findViewById(R.id.etObs);
        etLocal = (EditText) findViewById(R.id.etLocal);
        dataInicio = (EditText) findViewById(R.id.dataInicio);
        horaInicio = (EditText) findViewById(R.id.horaInicio);
        dataTermino = (EditText) findViewById(R.id.dataTermino);
        horaTermino = (EditText) findViewById(R.id.horaTermino);

        // Seta os valores padrão
        dataInicio.setText(prepareDatePart(Integer.toString(dia)) + "/"
                + prepareDatePart(Integer.toString((mes + 1))) + "/" + ano);
        dataTermino.setText(prepareDatePart(Integer.toString(dia)) + "/"
                + prepareDatePart(Integer.toString((mes + 1))) + "/" + ano);
        horaInicio.setText(prepareDatePart(Integer.toString(hora)) + ":"
                + prepareDatePart(Integer.toString(minuto)));
        horaTermino.setText(prepareDatePart(Integer.toString((hora + 1))) + ":"
                + prepareDatePart(Integer.toString(minuto)));
    }

    public void selecionarHora(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        // Passa como parametro o controle que chamou o 'manipulador'
        ((TimePickerFragment) newFragment).setEditText((EditText) view);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void selecionarData(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        // Passa como parametro o controle que chamou o 'manipulador'
        ((DatePickerFragment) newFragment).setEditText((EditText) view);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void addTarefa() {
        if (etTitulo.getText().toString().isEmpty()) {
            etTitulo.setError("A tarefa precisa de um título");
            etTitulo.requestFocus();
        } else {
            try {
                TarefaDao dao = new TarefaDao(this);
                Tarefa tarefa = new Tarefa();
                tarefa.setTar_titulo(etTitulo.getText().toString());
                tarefa.setTar_obs(etObs.getText().toString());
                tarefa.setLocal(etLocal.getText().toString());

                tarefa.setTar_inicioData(tarefa.prepareDate(dataInicio
                        .getText().toString(), horaInicio.getText().toString()));
                tarefa.setTar_terminoData(tarefa
                        .prepareDate(dataTermino.getText().toString(),
                                horaTermino.getText().toString()));
                dao.insertTarefa(tarefa);
                this.finish();

            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), ex.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_add_tarefa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btSalvar:
                addTarefa();
                break;
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }

        return true;
    }
}
