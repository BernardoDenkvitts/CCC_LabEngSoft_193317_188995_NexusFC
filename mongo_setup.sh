#!/bin/bash
echo "sleeping for 10 seconds"
sleep 10

echo mongo_setup.sh time now: `date +"%T" `
mongosh --host mongodb:27017 <<EOF
  var cfg = {
    "_id": "rs0",
    "version": 1,
    "members": [
      {
        "_id": 0,
        "host": "mongodb:27017",
        "priority": 2
      },
    ]
  };
  rs.initiate(cfg);
EOF

echo "4) Loading database schema…"
mongosh --host mongodb:27017 /scripts/init-nexusFC-schema.js

echo "✔ MongoDB replica set & schema ready!"