package com.example.demo.admin.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.admin.datas.actorData;
import com.example.demo.admin.repositories.adminActorRepository;

@Repository
public class adminActorJDBC implements adminActorRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<actorData> getAllActors(String filter) {
        List<actorData> actors = new ArrayList<>();

        if (filter != null) {
            actors = jdbc.query("SELECT * FROM actors WHERE name ILIKE ? AND deleted = false ORDER BY actor_id",
                    this::mapRowToActorData,
                    "%" + filter + "%");
        } else {
            actors = jdbc.query("SELECT * FROM actors WHERE deleted = false ORDER BY actor_id",
                    this::mapRowToActorData);
        }

        return actors;
    }

    @Override
    public List<actorData> getRemovedActors(String filter) {
        List<actorData> actors = new ArrayList<>();

        if (filter != null) {
            actors = jdbc.query("SELECT * FROM actors WHERE name ILIKE ? AND deleted = true ORDER BY actor_id",
                    this::mapRowToActorData,
                    "%" + filter + "%");
        } else {
            actors = jdbc.query("SELECT * FROM actors WHERE deleted = true ORDER BY actor_id", this::mapRowToActorData);
        }

        return actors;
    }

    @Override
    public void removeActor(int id) {
        jdbc.update("UPDATE actors SET deleted = true WHERE actor_id = ?", id);
    }

    @Override
    public void restoreActor(int id) {
        jdbc.update("UPDATE actors SET deleted = false WHERE actor_id = ?", id);
    }

    @Override
    public actorData getActorById(int id) {
        return jdbc.queryForObject("SELECT * FROM actors WHERE actor_id = ? AND deleted = false",
                this::mapRowToActorData, id);
    }

    @Override
    public List<actorData> getActorByMovieId(int id) {
        return jdbc.query(
                "SELECT * FROM movie_actors JOIN actors ON actors.actor_id = movie_actors.actor_id WHERE movie_id = ? AND deleted = false ORDER BY actors.actor_id",
                this::mapRowToActorData, id);
    }

    @Override
    public boolean updateActor(int id, String actor) {
        String sqlCheck = "SELECT COUNT(*) FROM actors WHERE name ILIKE ? and actor_id != ?";
        Integer count = jdbc.queryForObject(sqlCheck, Integer.class, actor, id);

        if (count != null && count > 0) {
            return false;
        }

        // System.out.println("actor id = " + id);

        jdbc.update("UPDATE actors SET name = ? WHERE actor_id = ?", actor, id);
        return true;
    }

    @Override
    public boolean addActor(String actor) {
        String sqlCheck = "SELECT COUNT(*) FROM actors WHERE name ILIKE ?";
        Integer count = jdbc.queryForObject(sqlCheck, Integer.class, actor);

        if (count != null && count > 0) {
            actorData deleted = jdbc.queryForObject("SELECT * FROM actors WHERE name ILIKE ? AND deleted = true",
                    this::mapRowToActorData, actor);

            if (deleted != null) {
                restoreActor(deleted.getActor_id());
                return true;
            } else {
                return false;
            }
        }

        jdbc.update("INSERT INTO actors (name) VALUES (?)", actor);
        return true;
    }

    private actorData mapRowToActorData(ResultSet rs, int rowNum)
            throws SQLException {
        // Create pemeriksaanData object using constructor
        return new actorData(
                rs.getInt("actor_id"),
                rs.getString("name"));
    }
}
