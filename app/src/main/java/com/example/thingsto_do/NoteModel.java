package com.example.thingsto_do;

public class NoteModel {


    private static String noteTitle;
    private static String noteTime;
    private static String noteDescription;


    NoteModel(String title, String noteTime, String description){

         NoteModel.noteTitle = title;
         NoteModel.noteTime = noteTime;
         NoteModel.noteDescription = description;
    }

    static String getNoteTitle() {
        return noteTitle;
    }

    public static void setNoteTitle(String noteTitle) {
        NoteModel.noteTitle = noteTitle;
    }

     static String getNoteTime() {

        return noteTime;
    }

    public static void setNoteTime(String noteTime) {
        NoteModel.noteTime = noteTime;
    }


    static String getDescription() {
        return noteDescription;
    }

    public static void setDescription(String description) {
        NoteModel.noteDescription = description;
    }

}