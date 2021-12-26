package ru.gb.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.gb.notes.recyclerview.NotesViewerAdapter;
import ru.gb.notes.repository.Constants;
import ru.gb.notes.repository.Note;
import ru.gb.notes.repository.Notes;
import ru.gb.notes.R;

public class NotesListActivity extends AppCompatActivity implements NotesViewerAdapter.OnNoteClickListener, Constants {

    private final Notes notes = Notes.getInstance();
    private RecyclerView notesViewer;
    private NotesViewerAdapter notesViewerAdapter;
    private ActivityResultLauncher<Intent> intentForResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_viewer);

        notesViewer = findViewById(R.id.notes_viewer);

        for (int i = 0; i < 10; i++) {
            notes.add("Заголовок " + i, "Текст заметки " + i);
        }

        notesViewerAdapter = new NotesViewerAdapter();
        notesViewerAdapter.setNotes(notes);
        notesViewerAdapter.setOnNoteClickListener(this);

        notesViewer = findViewById(R.id.notes_viewer);
        notesViewer.setLayoutManager(new LinearLayoutManager(this));
        notesViewer.setAdapter(notesViewerAdapter);

        intentForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            switch (result.getResultCode()) {
                case RESULT_OK:
                    Intent intent = result.getData();
                    if (intent != null) {
                        Note note = (Note) intent.getSerializableExtra(Constants.NOTE_UPDATE);
                        if (note != null) {
                            notes.addOrUpdateNote(note);
                            notesViewerAdapter.notifyDataSetChanged();
                            break;
                        }
                        note = (Note) intent.getSerializableExtra(Constants.NOTE_DELETE);
                        if (note != null) {
                            notes.delete(note.getId());
                            notesViewerAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case RESULT_CANCELED:
                    break;
            }
        });
    }


    @Override
    public void onNoteClick(Note note) {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        intent.putExtra(Constants.NOTE, note);
        intentForResult.launch(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_note:
                Intent intent = new Intent(this, NoteEditorActivity.class);
                intent.putExtra(Constants.NOTE, new Note(notes.getNewID(), "", ""));
                intentForResult.launch(intent);
                break;
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
