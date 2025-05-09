package com.nexusfc.api.Simulation;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.nexusfc.api.Model.ProfessionalPlayer;
import com.nexusfc.api.Model.ProfessionalTeam;
import com.nexusfc.api.Model.User;
import com.nexusfc.api.Model.UserTeam;
import com.nexusfc.api.Model.Component.ProfessionalPlayerEntry;
import com.nexusfc.api.Model.Enum.Lane;

public class TestData {

    private static String generateObjectId() {
        return new ObjectId().toHexString();
    }

    public static User createTestUser() {
        User user = new User();
        user.setId(generateObjectId());
        user.setName("John Doe");
        user.setEmail("john@gmail.com");
        user.setPassword("securePassword123");
        user.setCoins(100f);

        return user;
    }

    public static ProfessionalTeam createTestProfessionalTeam() {
        ProfessionalTeam team = new ProfessionalTeam();
        team.setId(generateObjectId());
        team.setName("Test Pro Team");
        team.setLeague("Test League");
        team.setRegion("Test Region");

        return team;
    }

    public static UserTeam createTestUserTeam(String userId) {
        UserTeam userTeam = new UserTeam();
        userTeam.setId(generateObjectId());
        userTeam.setName("Test User Team");
        userTeam.setUserId(new ObjectId(userId));

        ProfessionalTeam proTeam = createTestProfessionalTeam();
        List<ProfessionalPlayerEntry> entries = new ArrayList<>();
        entries.add(createEntry("TopPlayer", Lane.TOP, proTeam));
        entries.add(createEntry("JunglePlayer", Lane.JUNGLE, proTeam));
        entries.add(createEntry("MidPlayer", Lane.MID, proTeam));
        entries.add(createEntry("BotPlayer", Lane.ADC, proTeam));
        entries.add(createEntry("SupportPlayer", Lane.SUP, proTeam));

        userTeam.setProfessionalPlayers(entries);

        return userTeam;
    }

    private static ProfessionalPlayerEntry createEntry(
            String nick,
            Lane lane,
            ProfessionalTeam proTeam) {
        ProfessionalPlayer player = new ProfessionalPlayer();
        player.setId(generateObjectId());
        player.setNick(nick);
        player.setLane(lane);
        player.setTeam(proTeam);
        player.setMatchHistory(null);

        ProfessionalPlayerEntry entry = new ProfessionalPlayerEntry(player);
        entry.setIsStarter(true);

        return entry;
    }

}
