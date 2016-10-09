package net.etecassis.tcc2013.agenda;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

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
 * Classe do DatePicker e suas formatações de data
 *
 */


public class DatePickerFragment extends DialogFragment implements OnDateSetListener {
    private EditText editText;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    private String prepareDatePart(String part) {
        return (part.length() == 1) ? 0 + part : part;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String[] data = editText.getText().toString().split("/");
        int year = Integer.parseInt(data[2]);
        int month = Integer.parseInt(data[1]) - 1;
        int day = Integer.parseInt(data[0]);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        this.editText.setText(prepareDatePart(Integer.toString((dayOfMonth))) + "/" + prepareDatePart(Integer.toString(((monthOfYear + 1)))) + "/" + prepareDatePart(Integer.toString((year))));
    }
}
