package com.example.proyectogrupo1musicstore.NetworkTaksMulti;

import android.os.AsyncTask;

import com.example.proyectogrupo1musicstore.Models.PlayListItem;
import com.example.proyectogrupo1musicstore.Models.informacionGrupoGeneral;
import com.example.proyectogrupo1musicstore.NetworkTasks.GruposNetworkTasks.InfomacionGeneralGrupoAsyncTask;

import java.util.List;

public class inforMusicaPlaylistAsyncTask extends AsyncTask<String, Void, List<PlayListItem>> {
    private static final String TAG = "inforMusicaPlaylistAsyncTask";
    //private DataFetchListener dataFetchListener;

    @Override
    protected List<PlayListItem> doInBackground(String... strings) {
        return null;
    }
}
