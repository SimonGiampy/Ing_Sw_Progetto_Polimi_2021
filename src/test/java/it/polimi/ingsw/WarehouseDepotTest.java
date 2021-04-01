package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseDepotTest {

    @Test
    void enableAdditionalDepot() {

        WarehouseDepot depot = new WarehouseDepot();

        ArrayList<Resources> resources = new ArrayList<>();
        resources.add(Resources.COIN);
        resources.add(Resources.COIN);
        depot.enableAdditionalDepot(resources);

    }
}