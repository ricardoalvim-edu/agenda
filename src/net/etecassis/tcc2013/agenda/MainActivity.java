package net.etecassis.tcc2013.agenda;
/* Trabalho de Conclusão de Curso
 * Agenda para Android
 * 
 * Desenvolvedores: Vitor Oliveira, Leonardo Martins, Weveston Rezende e Ricardo Alvim
 *
 * Agradecimentos ao Desenvolvedor e Professor Marco Carvalho
 * 
 * Etec Pedro D'Arcádia Neto
 * Técnico em Informática
 * Turma do 1º Semestre de 2012
 * 
 * 
 * ricardoalvim.comoj.com/agenda
 *
 * Classe MainActivity - Atividade Principal da Agenda
 * 
 */

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import net.etecassis.tcc2013.agenda.dao.TarefaDao;
import net.etecassis.tcc2013.agenda.model.Tarefa;

public class MainActivity extends ListActivity implements OnNavigationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Intent: " + getIntent());
        //ActionBar
        actionBar();
        registerForContextMenu(getListView());
        Intent intent = getIntent();
        
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        	intent.setAction(Intent.ACTION_SEARCH);
        }
        handleIntent(intent);
    }

    //Cria a ActionBar
    protected void actionBar() {
        final ActionBar actionBar = getActionBar();
        //Título habilitado
        actionBar.setDisplayShowTitleEnabled(false);
        //Típo de navegação - List
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        //Cria os elementos da ListNavigation
        actionBar.setListNavigationCallbacks(
                new ArrayAdapter<String>(actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1, new String[]{
                        getString(R.string.todosLV),
                        getString(R.string.diaLV),
                        getString(R.string.semanaLV),
                        getString(R.string.mesLV),}), this);
    }


    //Trata as intenções na aplicação pegando como parâmetro uma intent pelo getIntent() do onCreate()
    @Override
    protected void onNewIntent(Intent intent) {
    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	}
    	setIntent(intent);
        handleIntent(intent);
    }

    //Trata a intent de pesquisa
    private void handleIntent(Intent intent) {
        //Se a intenção capturada for igual a uma do tipo ACTION_SEARCH...
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            //...É chamada uma query para pegar o texto que o usuário digitar
            String query = intent.getStringExtra(SearchManager.QUERY);

            //Criado um objeto do tipo TarefaDao
            TarefaDao dao = new TarefaDao(this);

	            /*  Instanciado um ArrayAdapter de Strings e passado para o método
                 *  listarTarefas a query a ser pesquisada
	             */
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1,
                    dao.listarTarefas(query));

            //Setado a list com o adaptador declarado
            setListAdapter(adapter);

        }
    }

    //Chama o editar no caso de pressionar uma tarefa
    @Override
    public void onListItemClick(ListView list, View view, int position, long id) {
        Intent intent;
        Tarefa tarefa = (Tarefa) getListAdapter().getItem(position);
        intent = new Intent(this, Editar.class);
        intent.putExtra("TAREFA", tarefa);
        startActivity(intent);
    }

    //Método padrão para criar menus de contexto
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.cmTitulo);
        inflater.inflate(R.menu.context, menu);
    }

    //Ações para o menu de contexto
    public boolean onContextItemSelected(MenuItem item) {

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        //Pega a posição do adaptador chamada
        final Tarefa tarefa = (Tarefa) getListAdapter().getItem(info.position);
        switch (item.getItemId()) {

            //Editar
            case R.id.edit:
                Intent intent = new Intent(this, Editar.class);
                intent.putExtra("TAREFA", tarefa);
                startActivity(intent);
                return true;

            //Deletar
            case R.id.delete:
                final TarefaDao dao = new TarefaDao(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.delete);
                builder.setMessage(R.string.avisoApagar);
                builder.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                    //onClick chamado em caso de uma resposta verdadeira
                    public void onClick(DialogInterface dialog, int which) {
                        //Passa o objeto tarefa como parâmetro ao método deleteTarefa
                        dao.deleteTarefa(tarefa);
                        dialog.dismiss();
                        onResume();
                    }
                });

                builder.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                    //onClick chamado em caso de uma resposta falsa
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    //Caso a atividade for requisitada novamente
    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getStringExtra(SearchManager.QUERY) == null) {
            TarefaDao dao = new TarefaDao(this);
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1,
                    dao.listTarefas(TarefaDao.SELECT_BY.ALL));
            setListAdapter(adapter);
        }
        if (Intent.ACTION_MAIN.equals(getIntent())){
        	TarefaDao dao = new TarefaDao(this);
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1,
                    dao.listTarefas(TarefaDao.SELECT_BY.ALL));
            setListAdapter(adapter);
        }
        
        
    }
    
    public void atualizarLista(){
    	TarefaDao dao = new TarefaDao(this);
        ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                android.R.layout.simple_list_item_1,
                dao.listTarefas(TarefaDao.SELECT_BY.ALL));
        setListAdapter(adapter);
    }

    //Chama o XML dos menus da ActionBar para a Atividade Principal
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // Pega a SearchView e seta a configuração para pesquisa
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.btSearch)
                .getActionView();
        // A atividade atual se torna uma atividade para pesquisa
        SearchableInfo si = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(si);
        // Do not iconify the widget; expand it by default
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        return true;

    }

    //Chama as ações para os itens da ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Adicionar
            case R.id.add:
                Intent i = new Intent(MainActivity.this, AddTarefa.class);
                startActivity(i);
                break;
            default:
                break;
        }
        return true;
    }

    // Implementação da interface 'onNavigationListener'
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        //Todas as Tarefas
        if (itemPosition == 0 && itemId == 0) {

            //Objeto TarefaDao
            TarefaDao dao = new TarefaDao(this);

        	/*		ArrayAdapter do tipo Tarefa
        	 * 		Com layout do tipo Simple List 
        	 * 		Pegando os dados através do objeto dao e chamando um método listTarefas()
        	 * 		E este chama a enumeração para pegar todas as tarefas no BD
        	 * 		O mesmo continua para o restante dos Se
        	 */
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1, dao.listTarefas(TarefaDao.SELECT_BY.ALL));

            //Seta o adaptador na lista
            setListAdapter(adapter);
        }

        //Dia
        else if (itemPosition == 1 && itemId == 1) {
            TarefaDao dao = new TarefaDao(this);
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1, dao.listTarefas(TarefaDao.SELECT_BY.DAY));
            setListAdapter(adapter);
        }

        //Semana
        else if (itemPosition == 2 && itemId == 2) {
            TarefaDao dao = new TarefaDao(this);
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1, dao.listTarefas(TarefaDao.SELECT_BY.WEEK));
            setListAdapter(adapter);
        }

        //Mês
        else if (itemPosition == 3 && itemId == 3) {
            TarefaDao dao = new TarefaDao(this);
            ArrayAdapter<Tarefa> adapter = new ArrayAdapter<Tarefa>(this,
                    android.R.layout.simple_list_item_1, dao.listTarefas(TarefaDao.SELECT_BY.MONTH));
            setListAdapter(adapter);
        }

        return false;
    }

}
