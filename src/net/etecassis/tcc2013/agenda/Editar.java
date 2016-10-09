package net.etecassis.tcc2013.agenda;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import net.etecassis.tcc2013.agenda.dao.TarefaDao;
import net.etecassis.tcc2013.agenda.model.Tarefa;

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
 * Classe Editar - Update, Restore e Delete
 *
 */


public class Editar extends Activity {

    //Atributos
    //Componentes da GUI
    private EditText etTitulo;
    private EditText etObs;
    private EditText etLocal;
    private EditText dataInicio;
    private EditText dataTermino;
    private EditText horaInicio;
    private EditText horaTermino;

    //Atributo do tipo Tarefa
    private Tarefa tarefa;

    //Método para popular o componente de data
    //em caso da data ter apenas um caracter, ela receberá um 0 na frente
    public String prepareDatePart(String part) {
        return (part.length() == 1) ? 0 + part : part;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setado a View XML
        setContentView(R.layout.activity_editar);

        //Serializado o objeto tarefa vindo da MainActivity no tarefa local
        this.tarefa = (Tarefa) getIntent().getSerializableExtra("TAREFA");

        //Constroi a ActionBar
        actionBar();


        // Vincula os atributos da classe aos controles do layout
        etTitulo = (EditText) findViewById(R.id.etTitulo);
        etObs = (EditText) findViewById(R.id.etObs);
        etLocal = (EditText) findViewById(R.id.etLocal);
        dataInicio = (EditText) findViewById(R.id.dataInicio);
        horaInicio = (EditText) findViewById(R.id.horaInicio);
        dataTermino = (EditText) findViewById(R.id.dataTermino);
        horaTermino = (EditText) findViewById(R.id.horaTermino);

        //Seta os valores aos controllers com valores do objeto tarefa
        etTitulo.setText(tarefa.getTar_titulo());
        etObs.setText(tarefa.getTar_obs());
        etLocal.setText(tarefa.getLocal());
        dataInicio.setText(tarefa.getFormatedDate(tarefa.getTar_inicioData()));
        horaInicio.setText(tarefa.getFormatedTime(tarefa.getTar_inicioData()));
        dataTermino.setText(tarefa.getFormatedDate(tarefa.getTar_terminoData()));
        horaTermino.setText(tarefa.getFormatedTime(tarefa.getTar_terminoData()));
    }

    //Método para editar hora
    public void selecionarHora(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        // Passa como parametro o controle que chamou o 'manipulador'
        ((TimePickerFragment) newFragment).setEditText((EditText) view);
        newFragment.show(getFragmentManager(), "timePicker");
    }

    //Método para editar a data
    public void selecionarData(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        // Passa como parametro o controle que chamou o 'manipulador'
        ((DatePickerFragment) newFragment).setEditText((EditText) view);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //Action Bar
    public void actionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.editarTarefa);
        actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //Método para ações dos itens da ActionBar
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Salvar
            case R.id.btSalvar:
                addTarefa();
                break;
            //Remover
            case R.id.btRemover:
                removerTarefa();
                break;
            //Voltar
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return true;
    }

    //Remover tarefa
    private void removerTarefa() {
        //Objeto do tipo TarefaDao
        final TarefaDao dao = new TarefaDao(this);
        //Cria um AlertDialog para confirmar a exclusão
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.avisoApagar);

        builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dao.deleteTarefa(tarefa);
                finish();
            }

        });

        builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.create().show();

    }

    //Adicionar (neste caso, para adicionar uma edição da tarefa)
    private void addTarefa() {
        if (etTitulo.getText().toString().isEmpty()) {
            //Verifica se o título está vazio
            etTitulo.setError("" + R.string.msgTitulo);
            etTitulo.requestFocus();
        }

        else {
            try {
                TarefaDao dao = new TarefaDao(this);
                tarefa.setTar_titulo(etTitulo.getText().toString());
                tarefa.setTar_obs(etObs.getText().toString());
                tarefa.setLocal(etLocal.getText().toString());
                tarefa.setTar_inicioData(tarefa.prepareDate(dataInicio.getText().toString(), horaInicio.getText().toString()));
                tarefa.setTar_terminoData(tarefa.prepareDate(dataTermino.getText().toString(), horaTermino.getText().toString()));
                dao.updateTarefa(tarefa);
                this.finish();

            } catch (Exception ex) {
                Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_editar, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
