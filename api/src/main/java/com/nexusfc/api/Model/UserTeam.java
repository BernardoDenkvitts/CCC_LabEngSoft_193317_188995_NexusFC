package com.nexusfc.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.Model.Enum.Lane;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "UserTeams")
public class UserTeam {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId userId;

    @Field("professional_players")
    private List<ProfessionalPlayerEntry> professionalPlayers = new ArrayList<>();

    public UserTeam(String userId) {
        this.userId = new ObjectId(userId);
        this.name = String.format("UserTeam%s", this.userId.toString().substring(0, 6)
                + this.userId.toString().substring(this.userId.toString().length() - 6));
    }

    public boolean hasPlayer(String playerId) {
        return professionalPlayers.stream().anyMatch(entry -> entry.getPlayer().getId().equals(playerId));
    }

    public boolean addPlayer(ProfessionalPlayerEntry playerEntry) {
        return professionalPlayers.add(playerEntry);
    }

    public boolean removePlayer(ProfessionalPlayerEntry playerEntry) {
        return professionalPlayers.remove(playerEntry);
    }

    public ProfessionalPlayerEntry getPlayerEntry(String playerId) {
        return professionalPlayers.stream()
                .filter(entry -> entry.getPlayer().getId().equals(playerId)).findFirst()
                .orElse(null);
    }

    public boolean hasCompleteTeam() {
        if (professionalPlayers.size() < 5)
            return false;

        EnumSet<Lane> coveredLanes = EnumSet.noneOf(Lane.class);

        for (ProfessionalPlayerEntry entry : professionalPlayers) {
            if (entry.getIsStarter()) {
                coveredLanes.add(entry.getPlayer().getLane());
            }
        }

        return coveredLanes.containsAll(EnumSet.allOf(Lane.class));
    }

    public boolean setStarter(String playerId) {
        ProfessionalPlayerEntry playerEntry = getPlayerEntry(playerId);
        if (playerEntry == null)
            return false;
        
        List<ProfessionalPlayerEntry> starters = getStarterPlayers();
        for (ProfessionalPlayerEntry entry : starters) {
            if (entry.getPlayer().getLane().equals(playerEntry.getPlayer().getLane())) {
                entry.setIsStarter(false);
                
                if (entry.getPlayer().getId().equals(playerId)) {
                    return true;
                }
                
                break;
            }
        }
        
        playerEntry.setIsStarter(true);
        return true;
    }

    @JsonIgnore
    public List<ProfessionalPlayerEntry> getStarterPlayers() {
        return professionalPlayers.stream().filter(entry -> entry.getIsStarter() == true).toList();
    }

}
