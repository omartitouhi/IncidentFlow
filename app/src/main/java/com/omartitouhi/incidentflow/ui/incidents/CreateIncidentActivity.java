package com.omartitouhi.incidentflow.ui.incidents;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.omartitouhi.incidentflow.R;

import java.util.HashMap;
import java.util.Map;

public class CreateIncidentActivity extends AppCompatActivity {

    private TextInputEditText titleInput;
    private TextInputEditText descriptionInput;
    private MaterialAutoCompleteTextView categoryInput;
    private MaterialAutoCompleteTextView priorityInput;
    private TextInputEditText buildingInput;
    private TextInputEditText floorInput;
    private TextInputEditText roomInput;
    private ImageView imagePreview;
    private Button chooseImageButton;
    private Button saveIncidentButton;
    private ProgressBar progressBar;

    private Uri selectedImageUri;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                selectedImageUri = uri;
                if (uri != null) {
                    imagePreview.setImageURI(uri);
                    imagePreview.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_incident);

        bindViews();
        setupDropdowns();

        if (!initFirebaseClients()) {
            saveIncidentButton.setEnabled(false);
            chooseImageButton.setEnabled(false);
            return;
        }

        chooseImageButton.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        saveIncidentButton.setOnClickListener(v -> attemptCreateIncident());
    }

    private void bindViews() {
        titleInput = findViewById(R.id.incidentTitleInput);
        descriptionInput = findViewById(R.id.incidentDescriptionInput);
        categoryInput = findViewById(R.id.incidentCategoryInput);
        priorityInput = findViewById(R.id.incidentPriorityInput);
        buildingInput = findViewById(R.id.incidentBuildingInput);
        floorInput = findViewById(R.id.incidentFloorInput);
        roomInput = findViewById(R.id.incidentRoomInput);
        imagePreview = findViewById(R.id.incidentImagePreview);
        chooseImageButton = findViewById(R.id.incidentChooseImageButton);
        saveIncidentButton = findViewById(R.id.incidentSaveButton);
        progressBar = findViewById(R.id.incidentProgressBar);
    }

    private void setupDropdowns() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.incident_categories,
                android.R.layout.simple_dropdown_item_1line
        );
        categoryInput.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.incident_priorities,
                android.R.layout.simple_dropdown_item_1line
        );
        priorityInput.setAdapter(priorityAdapter);
    }

    private boolean initFirebaseClients() {
        try {
            auth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();
            return true;
        } catch (IllegalStateException error) {
            Toast.makeText(this, R.string.incident_firebase_not_configured, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void attemptCreateIncident() {
        clearErrors();

        String title = valueOf(titleInput);
        String description = valueOf(descriptionInput);
        String category = valueOf(categoryInput);
        String priority = valueOf(priorityInput);
        String building = valueOf(buildingInput);
        String floor = valueOf(floorInput);
        String room = valueOf(roomInput);

        CreateIncidentValidator.ValidationResult validation = CreateIncidentValidator.validate(
                title,
                description,
                category,
                priority,
                building,
                floor,
                room
        );

        if (!validation.isValid()) {
            showFieldError(validation);
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, R.string.incident_user_not_connected, Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> incident = new HashMap<>();
        incident.put("title", title);
        incident.put("description", description);
        incident.put("category", category);
        incident.put("priority", priority);
        incident.put("building", building);
        incident.put("floor", floor);
        incident.put("room", room);
        incident.put("status", "Open");
        incident.put("createdAt", FieldValue.serverTimestamp());
        incident.put("createdBy", currentUser.getUid());
        incident.put("createdByEmail", currentUser.getEmail());
        incident.put("imageUri", selectedImageUri != null ? selectedImageUri.toString() : null);

        setLoading(true);
        firestore.collection("incidents")
                .add(incident)
                .addOnSuccessListener(docRef -> {
                    setLoading(false);
                    Toast.makeText(this, R.string.incident_create_success, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(error -> {
                    setLoading(false);
                    if (error instanceof FirebaseFirestoreException
                            && ((FirebaseFirestoreException) error).getCode() == FirebaseFirestoreException.Code.UNAVAILABLE) {
                        Toast.makeText(this, R.string.incident_error_network, Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(this, R.string.incident_error_generic, Toast.LENGTH_LONG).show();
                });
    }

    private void showFieldError(CreateIncidentValidator.ValidationResult validation) {
        switch (validation.getField()) {
            case TITLE:
                titleInput.setError(validation.getMessage());
                titleInput.requestFocus();
                break;
            case DESCRIPTION:
                descriptionInput.setError(validation.getMessage());
                descriptionInput.requestFocus();
                break;
            case CATEGORY:
                categoryInput.setError(validation.getMessage());
                categoryInput.requestFocus();
                break;
            case PRIORITY:
                priorityInput.setError(validation.getMessage());
                priorityInput.requestFocus();
                break;
            case BUILDING:
                buildingInput.setError(validation.getMessage());
                buildingInput.requestFocus();
                break;
            case FLOOR:
                floorInput.setError(validation.getMessage());
                floorInput.requestFocus();
                break;
            case ROOM:
                roomInput.setError(validation.getMessage());
                roomInput.requestFocus();
                break;
            default:
                Toast.makeText(this, validation.getMessage(), Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void clearErrors() {
        titleInput.setError(null);
        descriptionInput.setError(null);
        categoryInput.setError(null);
        priorityInput.setError(null);
        buildingInput.setError(null);
        floorInput.setError(null);
        roomInput.setError(null);
    }

    private String valueOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private String valueOf(MaterialAutoCompleteTextView input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void setLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        saveIncidentButton.setEnabled(!isLoading);
        chooseImageButton.setEnabled(!isLoading);
    }
}

