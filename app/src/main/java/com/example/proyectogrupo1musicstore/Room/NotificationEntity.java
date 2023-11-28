package com.example.proyectogrupo1musicstore.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class NotificationEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String body;
    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "userid")
    public Integer userid;

    @ColumnInfo(name = "groupid")
    public Integer groupid;

    @ColumnInfo(name = "tipo")
    public String tipo;
    @ColumnInfo(name = "timestamp")
    public long timestamp = System.currentTimeMillis();
}
