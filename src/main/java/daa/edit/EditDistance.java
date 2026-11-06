package daa.edit;

public interface EditDistance {
    int distance(String a, String b);

    long getSteps();

    long getMemoryCells();

    void reset();
}
