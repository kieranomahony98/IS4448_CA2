package com.example.myapplication.interfaces;

import com.example.myapplication.model.heroModel;

public interface CreateHero {
    public void onSuccess(heroModel hero);
    public void  onError(String message);
}
