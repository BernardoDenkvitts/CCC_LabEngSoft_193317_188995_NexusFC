db = db.getSiblingDB('nexusfc');

db.createCollection("ProfessionalTeams", {
  validator: {
    $jsonSchema: {
      "bsonType": "object",
      "title": "ProfessionalTeams",
      "properties": {
        "region": {
          "bsonType": "string"
        },
        "league": {
          "bsonType": "string"
        },
        "name": {
          "bsonType": "string"
        }
      }
    }
  },
  "autoIndexId": true
});
db.ProfessionalTeams.createIndex({"name": "text"}, {unique: true})

db.createCollection("ProfessionalPlayers", {
  validator: {
    $jsonSchema: {
      "bsonType": "object",
      "title": "ProfessionalPlayers",
      "properties": {
        "nick": {
          "bsonType": "string"
        },
        "lane": {
          "enum": ["TOP", "JUNGLE", "MID", "ADC", "SUP"]
        },
        "team": {
          "bsonType": "objectId"
        },
        "match_history": {
          "bsonType": "array",
          "items": {
            "title": "object",
            "properties": {
              "versus": {
                "bsonType": "string"
              },
              "champion": {
                "bsonType": "string"
              },
              "kills": {
                "bsonType": "string"
              },
              "deaths": {
                "bsonType": "string"
              },
              "assists": {
                "bsonType": "string"
              },
              "gold": {
                "bsonType": "string"
              },
              "cs": {
                "bsonType": "string"
              },
              "win": {
                "bsonType": "bool"
              },
              "vod": {
                "bsonType": "string"
              },
              "tournament": {
                "bsonType": "string"
              }
            }
          }
        },
        "overall_kill": {
          "bsonType": "decimal"
        },
        "overall_death": {
          "bsonType": "decimal"
        },
        "overall_assist": {
          "bsonType": "decimal"
        },
        "overall_win_rate": {
          "bsonType": "decimal"
        },
        "cost": {
          "bsonType": "decimal"
        }
      }
    }
  },
  "autoIndexId": true
});
db.ProfessionalPlayers.createIndex({"nick": "text"})

db.createCollection("Simulations", {
  validator: {
    $jsonSchema: {
      "bsonType": "object",
      "title": "Simulations",
      "required": ["desafiante_id", "desafiado_id"],
      "properties": {
        "versus_player": {
          "bsonType": "bool"
        },
        "desafiante_id": {
          "bsonType": "objectId"
        },
        "desafiado_id": {
          "bsonType": "objectId"
        },
        "status": {
          "enum": ["REQUESTED", "DENIED", "COMPLETED"]
        },
        "bet_value": {
          "bsonType": "decimal"
        },
        "created_at": {
          "bsonType": "date"
        },
        "win": {
          "bsonType": "bool"
        },
        "desafiante_team": {
          "bsonType": "array",
          "items": {
            "bsonType": "objectId"
          }
        },
        "desafiado_team": {
          "bsonType": "array",
          "items": {
            "bsonType": "objectId"
          }
        }
      }
    }
  },
  "autoIndexId": true
});
db.Simulations.createIndex({ "desafiante_id": 1 });
db.Simulations.createIndex({ "desafiado_id": 1 });

db.createCollection("Users", {
  validator: {
    $jsonSchema: {
      "bsonType": "object",
      "title": "Users",
      "properties": {
        "name": {
          "bsonType": "string"
        },
        "email": {
          "bsonType": "string"
        },
        "password": {
          "bsonType": "string"
        },
        "created_at": {
          "bsonType": "date"
        },
        "last_login": {
          "bsonType": "date"
        },
        "coins": {
          "bsonType": "decimal"
        }
      }
    }
  },
  "autoIndexId": true
});
db.Users.createIndex({"email": "text"}, {unique: true});

db.createCollection("UserTeams", {
  validator: {
    $jsonSchema: {
      "bsonType": "object",
      "title": "UserTeams",
      "required": ["user_id"],
      "properties": {
        "name": {
          "bsonType": "string"
        },
        "professional_players": {
          "bsonType": "array",
          "items": {
            "title": "object",
            "required": ["player_id"],
            "properties": {
              "player_id": {
                "bsonType": "objectId"
              },
              "is_starter": {
                "bsonType": "bool"
              }
            }
          }
        },
        "user_id": {
          "bsonType": "objectId"
        }
      }
    }
  },
  "autoIndexId": true
});
db.UserTeams.createIndex({"user_id": 1});
db.UserTeams.createIndex({"name": "text"}, {unique: true})

db.createCollection("Transactions", {
  validator: {
    $jsonSchema: {
      "bsonType": "object",
      "title": "Transactions",
      "required": ["user_id"],
      "properties": {
        "amount": {
          "bsonType": "decimal"
        },
        "status": {
          "enum": ["COMPLETED", "FAILED"]
        },
        "created_at": {
          "bsonType": "date"
        },
        "user_id": {
          "bsonType": "objectId"
        }
      }
    }
  },
  "autoIndexId": true
});
db.Transactions.createIndex({"user_id": 1});