package jsource.codegenerator;


/**
 * DefinitionGenerator.java	03/17/03
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 */

public abstract class DefinitionGenerator {

    public DefinitionGenerator(LanguageManager manager) {
        m_Manager = manager;
    }

    public String getMemberVarDefinition(Property m_Property) {
        StringBuffer definition = new StringBuffer();

        definition.append("\tprivate " + m_Manager.getTypes().getTypeString(m_Property.getType()) + " " + getMemberVar(m_Property) + ";\n");
        return definition.toString();
    }

    public String getHtmlMemberVarDefinition(Property m_Property) {
        StringBuffer definition = new StringBuffer();

        definition.append(emitKeyWord("private ") + emitTypeWord(m_Manager.getTypes().getTypeString(m_Property.getType())) + " " + getMemberVar(m_Property) + ";<br>");
        return definition.toString();
    }

    protected String emitKeyWord(String word) {
        return "<b><font color=\"blue\">" + word + "</font></b>";
    }

    protected String emitTypeWord(String word) {
        return "<font color=\"red\">" + word + "</font>";
    }

    public String getConstructor(Property props[]) {
        return getConstructors(null, props);
    }

    public String getConstructors(String class_name, Property props[]) {
        if (class_name == null || class_name.trim().equals(""))
            class_name = null;
        StringBuffer definition = new StringBuffer();

        definition.append(getConstructorsStartComment());
        definition.append(getMemberwiseConstructor(class_name, props));
        definition.append(getCopyConstructor(class_name, props));
        definition.append(getConstructorsEndComment());
        return definition.toString();
    }

    public String getMemberwiseConstructor(String class_name, Property props[]) {
        StringBuffer definition = new StringBuffer();

        definition.append("\t/**\n\t* Memberwise constructor\n\t*/\n");
        if (class_name == null || class_name.equals(""))
            class_name = "[Class name]";
        definition.append("\tpublic " + class_name + "( ");
        for (int i = 0; i < props.length; i++) {
            String tmp_var = m_Manager.getTypes().getTypeString(props[i].getType())
                    + " _" + props[i].getName().toLowerCase();

            definition.append(tmp_var);
            if (i < props.length - 1)
                definition.append(",");
            definition.append(" ");
        }

        definition.append(") {\n");
        for (int i = 0; i < props.length; i++) {
            String tmp_var = "_" + props[i].getName().toLowerCase();

            definition.append("\t\t" + getMemberVar(props[i]) + " = " + tmp_var + ";");
            if (i < props.length - 1)
                definition.append("\n");
        }

        definition.append("\t\n\t}\n");
        return definition.toString();
    }

    public String getCopyConstructor(String class_name, Property props[]) {
        StringBuffer definition = new StringBuffer();

        definition.append("\t/**\n\t* Copy constructor\n\t*/\n");
        if (class_name == null || class_name.equals(""))
            class_name = "[Class name]";
        definition.append("\tpublic " + class_name + "( " + class_name + " _from ) {\n");
        for (int i = 0; i < props.length; i++) {
            String member = getMemberVar(props[i]);

            definition.append("\t\tthis." + member + " = _from." + member + ";");
            if (i < props.length - 1)
                definition.append("\n");
        }

        definition.append("\t\n\t}\n");
        return definition.toString();
    }

    public String getClassDefinition(String class_name) {
        if (class_name == null || class_name.equals(""))
            class_name = "[Class name]";
        return "public class " + class_name + " {\n";
    }

    public abstract String getDefinition(Property property);

    public abstract String getHtmlDefinition(Property property);

    public abstract String getMemberVar(Property property);

    public abstract String toString(Property aproperty[]);

    public abstract String getMembersStartComment();

    public abstract String getMembersEndComment();

    public abstract String getAccessorsStartComment();

    public abstract String getAccessorsEndComment();

    public abstract String getConstructorsStartComment();

    public abstract String getConstructorsEndComment();

    protected LanguageManager m_Manager;
}
