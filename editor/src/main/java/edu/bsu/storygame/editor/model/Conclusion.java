package edu.bsu.storygame.editor.model;

public class Conclusion {
    public String text;
    public Integer points;

    public static Conclusion emptyConclusion() {
        return new Conclusion("");
    }

    public Conclusion(String text) {
        this(text, null);
    }

    public Conclusion(String text, Integer points) {
        this.text = text;
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conclusion that = (Conclusion) o;

        if (points != that.points) return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + points;
        return result;
    }
}
