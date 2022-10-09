import java.util.*;

public class PositionManager {

    private List<Position> allCurrentPositions;
    private List<Entity> allEntity;

    public PositionManager() {
        this.allCurrentPositions = new ArrayList<>();
        this.allEntity = new ArrayList<>();
    }

    public void addPosition(Position position){
        this.allCurrentPositions.add(position);
    }

    public void addEntity(Entity entity){
        this.allEntity.add(entity);
    }

    /**
     * Compute the new position the reach the arrival position
     * @param currentPosition entity current position
     * @param arrivalPosition entity arrival position
     * @return the new position, closest to the arrival
     */
    public static Position getNewPosition(Position currentPosition, Position arrivalPosition) {
        int iDifference = arrivalPosition.getI() - currentPosition.getI();
        int jDifference = arrivalPosition.getJ() - currentPosition.getJ();

        if(iDifference != 0) iDifference /= Math.abs(iDifference);
        if(jDifference != 0) jDifference /= Math.abs(jDifference);

        int xySum = iDifference + jDifference;

        if(xySum == -2) return new Position(currentPosition.getI()-1, currentPosition.getJ()-1);

        else if(xySum == 2) return new Position(currentPosition.getI()+1, currentPosition.getJ()+1);

        else if(xySum == 0 && iDifference == 1) return new Position(currentPosition.getI()+1, currentPosition.getJ()-1);

        else if(xySum == 0 && iDifference == -1) return new Position(currentPosition.getI()-1, currentPosition.getJ()+1);

        else if(xySum == 1 && iDifference == 1) return new Position(currentPosition.getI()+1, currentPosition.getJ());

        else if(xySum == -1 && iDifference == -1) return new Position(currentPosition.getI()-1, currentPosition.getJ());

        else if(xySum == 1 && iDifference == 0) return new Position(currentPosition.getI(), currentPosition.getJ()+1);

        else
            return new Position(currentPosition.getI(), currentPosition.getJ()-1); //if(xySum == -1 && iDifference == 0)
    }

    /**
     * Check if a position is taken or not to avoid overlay spawning entities
     * @param grid the grid
     * @param currentPosition spawning position
     * @return true if the position is already taken
     */
    static boolean isPositionTaken(Grid grid, Position currentPosition) {
        for (Entity entity : grid.entitiesList) {
            if(entity.getCurrentPosition().equals(currentPosition))
                return true;
        }
        return false;
    }

    /**
     * Get random spawning position
     * @param maxLength max length value available
     * @param maxHeight max height value available
     * @return a random position
     */
    public static Position getRandomPosition(int maxLength, int maxHeight) {
        Random random = new Random();
        return new Position(random.nextInt(0, maxLength), random.nextInt(0, maxHeight));
    }

    public List<Position> getAllCurrentPositions() {
        return allCurrentPositions;
    }

    public boolean positionIsAlreadyTaken(Position position){
        return this.allCurrentPositions.contains(position);
    }

    public void updatePositionOfEntity(Position currentPosition, Position newPosition){
        this.allCurrentPositions.remove(currentPosition);
        this.allCurrentPositions.add(newPosition);
    }

    public void destroyPosition(Position position) {
        this.allCurrentPositions.remove(position);
    }

    public boolean listOfAllPositionsIsEmpty() {
        return this.allCurrentPositions.size() == 0;
    }

    public Optional<Entity> findEntityAtAPosition(Position position){
        for(Entity entity: allEntity){
            if(entity.getCurrentPosition().equals(position))return Optional.of(entity);
        }
        return Optional.empty();
    }

    public boolean doTheEntityWantToGoToPosition(Position currentPosition, Position otherCurrentPosition, Position otherArrivalPosition) {
        Position futurPosition = getNewPosition(otherCurrentPosition, otherArrivalPosition);
        return currentPosition.equals(futurPosition);
    }

    public boolean isThereAConflict(Position currentPosition, Position otherPosition) {
        Optional<Entity> optionalEntity = findEntityAtAPosition(otherPosition);
        Entity conflictEntity = optionalEntity.get();
        return doTheEntityWantToGoToPosition(currentPosition, conflictEntity.getCurrentPosition(), conflictEntity.getArrivalPosition());
    }

    public void manageConflict(Entity entity, Position conflictPosition){
        Optional<Entity> optionalEntity = findEntityAtAPosition(conflictPosition);
        Entity conflictEntity = optionalEntity.get();
        Entity entityThatWillBeKill = null;
        if(entity.getId() < conflictEntity.getId())
            entityThatWillBeKill = entity;
        else
            entityThatWillBeKill = conflictEntity;
        entityThatWillBeKill.kill();
    }


    public boolean canEntityBeRevive(Entity entity) {
        boolean whereIWantToGoIsFree = !positionIsAlreadyTaken(getNewPosition(entity.getCurrentPosition(), entity.getArrivalPosition()));
        boolean whereIAmIsOccuped = positionIsAlreadyTaken(entity.getCurrentPosition());
        boolean ImKilled = entity.isKilled();
        return whereIWantToGoIsFree && !whereIAmIsOccuped && ImKilled;
    }

    public void removePosition(Position currentPosition) {
        this.allCurrentPositions.remove(currentPosition);
    }
}