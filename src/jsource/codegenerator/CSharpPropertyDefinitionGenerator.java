package jsource.codegenerator;


/**
 * CSharpPropertyDefinitionGenerator.java 03/17/03
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

public class CSharpPropertyDefinitionGenerator extends DefinitionGenerator {

    public CSharpPropertyDefinitionGenerator(LanguageManager manager) {
        super(manager);
    }

    public String getDefinition(Property m_Property) {
        StringBuffer definition = new StringBuffer();

        definition.append("\tpublic " + super.m_Manager.getTypes().getTypeString(m_Property.getType()) + " " + m_Property.getName() + "\n\t{\n");
        definition.append("\t\tget{ return " + super.m_Manager.getGenerator().getMemberVar(m_Property) + "; }\n");
        if (!m_Property.isReadOnly())
            definition.append("\t\tset{ " + super.m_Manager.getGenerator().getMemberVar(m_Property) + " = value; }");
        definition.append("\n\t}\n");
        return definition.toString();
    }

    public String getHtmlDefinition(Property m_Property) {
        StringBuffer definition = new StringBuffer();

        definition.append(emitKeyWord("public ") + emitTypeWord(super.m_Manager.getTypes().getTypeString(m_Property.getType())) + " " + m_Property.getName() + "<br>\t{");
        definition.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;" + emitKeyWord("get") + "{ " + emitKeyWord("return ") + super.m_Manager.getGenerator().getMemberVar(m_Property) + "; }");
        if (!m_Property.isReadOnly())
            definition.append("<br>&nbsp;&nbsp;&nbsp;&nbsp;" + emitKeyWord("set") + "{ " + super.m_Manager.getGenerator().getMemberVar(m_Property) + " = " + emitKeyWord("value") + "; }");
        definition.append("<br>}");
        return definition.toString();
    }

    public String getMemberVar(Property p) {
        switch (p.getType()) {
        case 5: // '\005'
            return "m_i" + p.getName();

        case 6: // '\006'
            return "m_ui" + p.getName();

        case 7: // '\007'
            return "m_l" + p.getName();

        case 8: // '\b'
            return "m_ul" + p.getName();

        case 9: // '\t'
            return "m_c" + p.getName();

        case 3: // '\003'
            return "m_si" + p.getName();

        case 4: // '\004'
            return "m_usi" + p.getName();

        case 10: // '\n'
            return "m_f" + p.getName();

        case 11: // '\013'
            return "m_d" + p.getName();

        case 12: // '\f'
            return "m_z" + p.getName();

        case 13: // '\r'
            return "m_dec" + p.getName();

        case 2: // '\002'
            return "m_b" + p.getName();

        case 1: // '\001'
            return "m_bs" + p.getName();

        case 14: // '\016'
            return "m_obj" + p.getName();

        case 15: // '\017'
            return "m_s" + p.getName();
        }
        throw new RuntimeException("Unsuported type");
    }

    public String toString(Property props[]) {
        StringBuffer main_definition = new StringBuffer("\t/**\n\t* Override of Object#ToString()\n\t*/\n");

        main_definition.append("\tpublic override String ToString()\n\t{\n");
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
        main_definition.append("\n\t}");
        return main_definition.toString();
    }

    public String getMembersStartComment() {
        return "\t#region Private member variables\n";
    }

    public String getMembersEndComment() {
        return "\t#endregion\n\n";
    }

    public String getAccessorsStartComment() {
        return "\t#region Properties\n";
    }

    public String getAccessorsEndComment() {
        return "\t#endregion\n\n";
    }

    public String getConstructorsStartComment() {
        return "\t#region Constructors\n";
    }

    public String getConstructorsEndComment() {
        return "\t#endregion\n\n";
    }
}
