package fm.pattern.tokamak.authorization;

@SuppressWarnings("unchecked")
abstract class AbstractDSL<T, C> {

    private boolean persistent;

    public T thatIs() {
        return (T) this;
    }

    public T thatAre() {
        return (T) this;
    }

    public T and() {
        return (T) this;
    }

    public T persistent() {
        this.persistent = true;
        return (T) this;
    }

    public boolean shouldPersist() {
        return persistent;
    }

    public static void pause(Integer milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public abstract C build();

}
