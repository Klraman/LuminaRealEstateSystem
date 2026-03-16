package MyLib;

import java.util.ArrayList;
import java.util.List;

public class PropertyCatalog {

    private List<PropertyUnit> availableUnits;

    public PropertyCatalog() {
        availableUnits = new ArrayList<>();
        // PropertyCatalog is the ONLY class that instantiates concrete property types
        registerUnit(new angeliqueInner());
        registerUnit(new angeliqueEnd());
        registerUnit(new angeliInner());
        registerUnit(new angeliEnd());
        registerUnit(new aimeeInner());
        registerUnit(new aimeeEnd());
    }

    public void registerUnit(PropertyUnit unit) {
        availableUnits.add(unit);
    }

    public List<PropertyUnit> getAllUnits() {
        return new ArrayList<>(availableUnits);
    }

    public List<PropertyUnit> getUnitsByPhase(Phase phase) {
        List<PropertyUnit> result = new ArrayList<>();
        for (PropertyUnit unit : availableUnits) {
            if (unit.getPhase() == phase) {
                result.add(unit);
            }
        }
        return result;
    }

    public List<PropertyUnit> getUnitsByBudget(double budget) {
        List<PropertyUnit> result = new ArrayList<>();
        for (PropertyUnit unit : availableUnits) {
            if (unit.getEstimatedTCP() <= budget) {
                result.add(unit);
            }
        }
        return result;
    }

    public PropertyUnit getUnitByName(String modelName) {
        for (PropertyUnit unit : availableUnits) {
            if (unit.getModelName().equalsIgnoreCase(modelName)) {
                return unit;
            }
        }
        return null;
    }
}
