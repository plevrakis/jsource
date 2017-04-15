package jsource.codegenerator;


/**
 * JavaPropertyDefinitionGenerator.java	03/17/03
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

import jsource.util.JSConstants;


public class JavaPropertyDefinitionGenerator extends DefinitionGenerator {

	private String lineSep = JSConstants.LINE_SEP;

    public JavaPropertyDefinitionGenerator(LanguageManager manager) {
        super(manager);
    }

    public String getDefinition(Property m_Property) {
        StringBuffer definition = new StringBuffer();

        definition.append("\tpublic " + super.m_Manager.getTypes().getTypeString(m_Property.getType()) + " get" + m_Property.getName() + "() {");
        definition.append("" + lineSep + "\t\treturn " + super.m_Manager.getGenerator().getMemberVar(m_Property) + "; " + lineSep + "\t}" + lineSep);
        if (!m_Property.isReadOnly()) {
            definition.append("\tpublic void set" + m_Property.getName() + "(" + super.m_Manager.getTypes().getTypeString(m_Property.getType()) + " _value ) {");
            definition.append("" + lineSep + "\t\t" + super.m_Manager.getGenerator().getMemberVar(m_Property) + " = _value; " + lineSep + "\t}" + lineSep);
        }
        return definition.toString();
    }

    public String getHtmlDefinition(Property m_Property) {
        StringBuffer definition = new StringBuffer();

        definition.append(emitKeyWord("public ") + emitTypeWord(super.m_Manager.getTypes().getTypeString(m_Property.getType())) + " get" + m_Property.getName() + "() {");
        definition.append(emitKeyWord("return ") + super.m_Manager.getGenerator().getMemberVar(m_Property) + "; }" + lineSep + "<br>");
        if (!m_Property.isReadOnly()) {
            definition.append(emitKeyWord("public void ") + "set" + m_Property.getName() + "(" + emitTypeWord(super.m_Manager.getTypes().getTypeString(m_Property.getType())) + " _value ) {");
            definition.append(super.m_Manager.getGenerator().getMemberVar(m_Property) + " = _value; }" + lineSep + "<br>");
        }
        return definition.toString();
    }

    public String getMemberVar(Property p) {
        switch (p.getType()) {

        case 1:
            return "m_z" + p.getName();

        case 2:
            return "m_c" + p.getName();

        case 3:
            return "m_si" + p.getName();

        case 4:
            return "m_b" + p.getName();

        case 5:
            return "m_i" + p.getName();

        case 6:
            return "m_l" + p.getName();

        case 7:
            return "m_f" + p.getName();

        case 8:
            return "m_d" + p.getName();

        case 9:
        	return "m_o" + p.getName();

        case 10:
            return "m_s" + p.getName();

        default:
            throw new RuntimeException("Unsupported type");
        }
    }

    public String toString(Property props[]) {
        StringBuffer main_definition = new StringBuffer("\t/**" + lineSep + "\t* Override of Object#toString()" + lineSep + "\t*/" + lineSep);

        main_definition.append("\tpublic String toString() {" + lineSep);
        StringBuffer definition = new StringBuffer();

        for (int i = 0; i < props.length; i++) {
            String tmp_var = super.m_Manager.getGenerator().getMemberVar(props[i]);

            if (i == 0)
                definition.append("\"{");
            definition.append("[" + tmp_var + " = \" + " + tmp_var + " + \"]");
            if (i == props.length - 1)
                definition.append("}\"");
            if (i < props.length - 1)
                definition.append(", ");
        }

        main_definition.append("\t\treturn " + definition.toString() + ";");
        main_definition.append(lineSep + "\t}");
        return main_definition.toString();
    }

    public String getMembersStartComment() {
        return "\t/**" + lineSep + "\t* Member declarations " + lineSep + "\t*/" + lineSep;
    }

    public String getMembersEndComment() {
        return lineSep;
    }

    public String getAccessorsStartComment() {
        return "\t/**" + lineSep + "\t* Public access methods definition" + lineSep + "\t*/" + lineSep;
    }

    public String getAccessorsEndComment() {
        return lineSep;
    }

    public String getConstructorsStartComment() {
        return lineSep;
    }

    public String getConstructorsEndComment() {
        return lineSep;
    }
}
