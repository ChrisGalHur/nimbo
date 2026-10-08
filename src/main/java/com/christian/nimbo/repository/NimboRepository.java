package com.christian.nimbo.repository;

import java.util.Map;

public interface NimboRepository {

    //region Public test snapshots
    void savePublicTestSnapshot(
            Map<String, Object> snapshot
    );

    Map<String, Object> findPublicTestSnapshotById(
            String id
    );
    //endregion
}