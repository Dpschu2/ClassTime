package io.github.httpsdpschu2.classtime.managers;

import android.content.Context;
import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by Dean on 2/18/17.
 */
public class RealmManager {

    private static int CURRENT_SCHEMA_VERSION = 2;

    private static RealmManager mInstance;

    private RealmManager() {}

    public static RealmManager getInstance() {
        if(mInstance==null) {
            mInstance = new RealmManager();
        }
        return mInstance;
    }

    public Realm getRealm(Context context) {
        return Realm.getInstance(getConfig(context));
    }

    public RealmConfiguration getConfig(Context context) {
        return new RealmConfiguration.Builder(context)
                .schemaVersion(CURRENT_SCHEMA_VERSION)
                .migration(migration)
                .deleteRealmIfMigrationNeeded()
                .build();
    }


    // Migration
    static RealmMigration migration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            long count = realm.where("Reminder").count();
            Log.i("count", String.valueOf(count));
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();

//            // Migrate to version 1: Add a new class.
//            // Example:
//            // public Person extends RealmObject {
//            //     private String name;
//            //     private int age;
//            //     // getters and setters left out for brevity
//            // }
//            if (oldVersion == 0) {
//                schema.create("Activity")
//                        .addField("name", String.class, FieldAttribute.REQUIRED)
//                        .addField("description", String.class)
//                        .addField("multiplier", double.class);
//                schema.create("Schedule")
//                        .addField("name", String.class, FieldAttribute.REQUIRED)
//                        .addField("duration", int.class)
//                        .addRealmListField("activities", schema.get("Activity"));
//                oldVersion++;
//            }
//
//            // Migrate to version 2: Add a primary key + object references
//            // Example:
//            // public Person extends RealmObject {
//            //     private String name;
//            //     @PrimaryKey
//            //     private int age;
//            //     private Dog favoriteDog;
//            //     private RealmList<Dog> dogs;
//            //     // getters and setters left out for brevity
//            // }
            if (oldVersion == 1) {
                schema.get("class_reminder")
                        .addField("progress", String.class)
                        .addField("countGoal", int.class)
                        .addField("timeGoal", int.class);

                oldVersion++;
            }

//            if (oldVersion == 2) {
//schema.get("Repeat")
//        .addField("repeating", boolean.class);
//
//            }
        }
    };
}
