package net.etecassis.tcc2013.agenda;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

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
 * Classe do TimePicker e suas formatações de hora
 *
 */

public class TimePickerFragment extends DialogFragment implements
        OnTimeSetListener {
    private EditText editText;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    private String prepareDatePart(String part) {
        return (part.length() == 1) ? 0 + part : part;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        String[] hora = editText.getText().toString().split(":");
        int hour = Integer.parseInt(hora[0]);
        int minute = Integer.parseInt(hora[1]);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.editText.setText(prepareDatePart(Integer.toString(hourOfDay)) + ":" + prepareDatePart(Integer.toString(minute)));
    }
}
