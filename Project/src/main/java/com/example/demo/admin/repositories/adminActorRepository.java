package com.example.demo.admin.repositories;

import java.util.List;

import com.example.demo.admin.datas.actorData;

public interface adminActorRepository {
    List<actorData> getAllActors(String filter);

    List<actorData> getRemovedActors(String filter);

    void removeActor(int id);

    void restoreActor(int id);

    actorData getActorById(int id);

    List<actorData> getActorByMovieId(int id);

    boolean updateActor(int id, String actor);

    boolean addActor(String actor);
}
