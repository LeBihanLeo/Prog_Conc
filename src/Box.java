import java.util.Optional;

/**
 * Box which contains an entity
 */
public class Box {

    //box position in the grid
    final Position boxPosition;
    //entity in the box
    Optional<Entity> entity;

    public Box(Position position) {
        this.boxPosition = position;
        this.entity = Optional.empty();
    }

    /**
     * Put the entity in the box if it is empty, otherwise die if the entity id is smaller or wait if its id is greater
     * than the one of the entity already in the box
     * @param entity that wants to go in the box
     * @return a state MOVE, DIE ord IS_WAITING depending on the situation described above
     */
    synchronized MovementState arrive(Entity entity) {
        if (this.entity.isEmpty()) {
            Simulation.grid.getBox(entity.getCurrentPosition().getI(), entity.getCurrentPosition().getJ()).depart();
            this.entity = Optional.of(entity);
            return MovementState.MOVE;
        }
        else if (entity.getId() < this.entity.get().getId())
            return MovementState.DIE;
        return MovementState.IS_WAITING;
    }

    /**
     * Freed the box so another entity can go in
     */
    synchronized void depart() {
        this.entity = Optional.empty();
        notifyAll();
    }

    /**
     * Put the entity in the box.
     * In the case one thread per entity:
     * If the box is not empty, the entity (thread) waits.
     * @param entity entity to put in the box
     * @throws InterruptedException exception
     */
    synchronized void setEntity(Entity entity) throws InterruptedException {
        while (this.entity.isPresent()){
            wait();
        }
        this.entity = Optional.of(entity);
    }

    public Optional<Entity> getEntity() {
        return entity;
    }
}
