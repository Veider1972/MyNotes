package ru.gb.notes.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import ru.gb.notes.R;
import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;

public class NoteEditorActivity extends AppCompatActivity {
    private TextInputEditText titleTextInputEditText;
    private TextInputEditText descriptionTextInputEditText;
    private MaterialButton acceptButton;
    private MaterialButton deleteButton;
    private MaterialButton cancelButton;
    private int id = -1;
    private Note note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_editor);
        titleTextInputEditText = findViewById(R.id.title_text_editor);
        descriptionTextInputEditText = findViewById(R.id.description_text_editor);
        acceptButton = findViewById(R.id.accept_button);
        deleteButton = findViewById(R.id.delete_button);
        cancelButton = findViewById(R.id.cancel_button);
        acceptButton.setOnClickListener(view -> {
            note.setTitle(titleTextInputEditText.getText().toString());
            note.setDescription(descriptionTextInputEditText.getText().toString());

            Intent intent = new Intent();
            intent.putExtra(Constants.NOTE_UPDATE, note);
            setResult(RESULT_OK, intent);
            finish();
        });
       deleteButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(Constants.NOTE_DELETE, note);
            setResult(RESULT_OK, intent);
            finish();
        });
        cancelButton.setOnClickListener(view -> {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            finish();
        });

        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra(Constants.NOTE);
        if (note.getTitle() != null)
        titleTextInputEditText.setText(note.getTitle());
        if (note.getDescription() != null)
        descriptionTextInputEditText.setText(note.getDescription());
        id = note.getId();

    }
}
