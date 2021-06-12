package com.vokasi.mecanic;

import java.util.ArrayList;
import java.util.List;

public interface IFirebaseSavedData {
    void onFirebaseLoadSuccess(List<ModelKota> listKota);
    void onFirebaseLoadFailed (String message);
}
