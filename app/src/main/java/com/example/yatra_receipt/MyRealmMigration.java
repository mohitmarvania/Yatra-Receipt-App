package com.example.yatra_receipt;

import java.util.Objects;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // Get the schema for the current Realm version
        RealmSchema schema = realm.getSchema();
        // Perform migration based on the old and new versions
        if (oldVersion == 0) {
            // Migration for version 0 to version 1
            // Remove the 'tarikh' property
            schema.get("Data")
                    .removeField("tarikh");

            // Add the new properties 'deposit', 'baki', and 'svikarnar'
            schema.get("Data")
                    .addField("deposit", String.class)
                    .addField("baki", String.class)
                    .addField("svikarnar", String.class);
            // Increment the schema version
            oldVersion++;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return  true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MyRealmMigration other = (MyRealmMigration) obj;
        return getVersion() == other.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVersion());
    }

    private int getVersion() {
        return 1;
    }
}
