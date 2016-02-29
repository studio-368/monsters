package edu.bsu.storygame.editor.model;

public class Conclusion {
    public String text;
    public Integer points;
    public String skill;

    public static Conclusion emptyConclusion() {
        return new Conclusion("");
    }

    public Conclusion(String text) {
        this(text, null, null);
    }

    public Conclusion(String text, Integer points, String skill) {
        this.text = text;
        this.points = points;
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Conclusion that = (Conclusion) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;
        if (points != null ? !points.equals(that.points) : that.points != null) return false;
        return skill != null ? skill.equals(that.skill) : that.skill == null;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (points != null ? points.hashCode() : 0);
        result = 31 * result + (skill != null ? skill.hashCode() : 0);
        return result;
    }
}
