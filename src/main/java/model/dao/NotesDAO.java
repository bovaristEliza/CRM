package model.dao;

import model.mo.Notes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;


public interface NotesDAO {

    Notes create(

            String content,
            String idmanager,
            String idcustomer
    );

    ArrayList<Notes> show(Integer id);

    void deleteNote(Integer idNote);
}
