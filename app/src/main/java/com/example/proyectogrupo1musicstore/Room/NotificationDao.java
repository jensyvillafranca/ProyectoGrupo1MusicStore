package com.example.proyectogrupo1musicstore.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    List<NotificationEntity> getAllNotifications();

    @Insert
    void insertNotification(NotificationEntity notification);

    @Query("DELETE FROM notifications WHERE id = :notificationId")
    void deleteNotificationById(int notificationId);
}
