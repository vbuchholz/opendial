

a_m = ["AskRepeat", "Confirm", "Disconfirm", "None", "Describe([])", "Describe([o1])", "Describe([o2])", 
       "Describe([o2, o1])", "Do(Move(Left))", "Do(Move(Right))", "Do(Move(Forward))", "Do(Move(Backward))", 
       "Do(PickUp(o1))", "Do(PickUp(o2))", "Do(Release(o1))", "Do(Release(o2))", "Excuse(DoNotSeeObject)", 
       "Excuse(DoNotCarryObject)", "Excuse(AlreadyCarryObject)", "Confirm(Move(Left))", "Confirm(Move(Right))", 
       "Confirm(Move(Forward))", "Confirm(Move(Backward))",  "Confirm(PickUp(o1))", "Confirm(PickUp(o2))", 
       "Confirm(Release(o1))", "Confirm(Release(o2))", "Ground(Move(Left))", "Ground(Move(Right))", 
       "Ground(Move(Forward))", "Ground(Move(Backward))",  "Ground(PickUp(o1))", "Ground(PickUp(o2))", 
       "Ground(Release(o1))", "Ground(Release(o2))", "Confirm(WhatDoYouSee)", "Confirm(DoYouSee(o1))", 
       "Confirm(DoYouSee(o2))"]

i_u = ["Move(Left)", "Move(Right)", "Move(Forward)", "Move(Backward)", "PickUp(o1)", "PickUp(o2)", "Release(o1)", 
       "Release(o2)", "WhatDoYouSee", "DoYouSee(o1)", "DoYouSee(o2)", "None"]

a_u = ["Confirm", "Disconfirm", "Nothing", "None", "Move(Left)", "Move(Right)", "Move(Forward)", "Move(Backward)", 
       "PickUp(o1)", "PickUp(o2)", "Release(o1)", "Release(o2)", "RepeatLast", "WhatDoYouSee", "DoYouSee(o1)",
       "DoYouSee(o2)"]

prelude = """
<variable id="theta_1">
<distrib type="dirichlet">
<alpha>300</alpha>
<alpha>300</alpha>
<alpha>300</alpha>
<alpha>300</alpha>
<alpha>300</alpha>
<alpha>300</alpha>
<alpha>300</alpha>
<alpha>300</alpha>
</distrib>
</variable>

<variable id="theta_2">
<distrib type="dirichlet">
<alpha>50</alpha>
<alpha>300</alpha>
</distrib>
</variable>

<variable id="theta_3">
<distrib type="dirichlet">
<alpha>5</alpha>
<alpha>300</alpha>
</distrib>
</variable>

<variable id="theta_4">
<distrib type="dirichlet">
<alpha>50</alpha>
<alpha>50</alpha>
<alpha>300</alpha>
</distrib>
</variable>

<variable id="theta_5">
<distrib type="dirichlet">
<alpha>5</alpha>
<alpha>5</alpha>
<alpha>300</alpha>
</distrib>
</variable>

<variable id="theta_14">
<distrib type="dirichlet">
<alpha>300</alpha>
<alpha>300</alpha>
</distrib>
</variable>\n\n"""

import os

def checkCompleteness():
    for i in range(1,300):
 	filename = "structinf/is2013-structinf-"+str(i)+".txt"
        if os.path.isfile(filename):
            lines = open(filename).readlines()
            for l in lines:
                if "i_u=" in l:
                    possiu= l.split("i_u=")[1].split("):")[0].split(" ")[0]
                    if possiu not in i_u:
                        print possiu
                if "a_m=" in l:
                    possam = l.split("a_m=")[1].split(" ^")[0].replace("\n", "")
                    if possam not in a_m:
                        print possam
                if "a_u=" in l:
                    possau = l.split("a_u=")[1].split("):")[0].split(" ")[0]
                    if possau not in a_u:
                        print possau


checkCompleteness()

def writePlainUserModel():
    file = open("domain_user_plain.xml", 'w')
    text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    text = text + "\n<model type=\"prediction\" trigger=\"i_u\">\n\n"
    text = text + "<rule>\n"
    for a in a_m:
        for i in i_u:
            text = text + "<case>\n<condition>\n"
            text = text + "<if var=\"a_m\" value=\"" + a +"\" relation=\"=\"/>\n"
            text = text + "<if var=\"i_u\" value=\"" + i +"\" relation=\"=\"/>\n"
            text = text + "</condition>\n"
            param = "theta_(a_m="+a + "^i_u="+i + ")"
            incr = 0
            for u in a_u:
                text = text + "<effect prob=\"" + param + "[" + str(incr)+ "]\">\n"
                text = text + "<set var=\"a_u\" value=\""+u+"\"/>\n"
                text = text + "</effect>\n"
                incr = incr+1
            text = text + "</case>\n"
    text = text + "\n</rule>\n</model>\n"
    file.write(text)
    
    
    
    
def writeLinearUserModel():
    file = open("domain_user_linear.xml", 'w')
    text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    text = text + "\n<model type=\"prediction\" trigger=\"i_u\">\n\n"
    text = text + "<rule>\n"
    for a in a_m:
        for i in i_u:
            text = text + "<case>\n<condition>\n"
            text = text + "<if var=\"a_m\" value=\"" + a +"\" relation=\"=\"/>\n"
            text = text + "<if var=\"i_u\" value=\"" + i +"\" relation=\"=\"/>\n"
            text = text + "</condition>\n"
            for u in a_u:
                param = "theta_(a_m="+a + "^a_u="+u+")+theta_(i_u="+i + "^a_u=" + u + ")"
                text = text + "<effect prob=\"" + param +"\">\n"
                text = text + "<set var=\"a_u\" value=\""+u+"\"/>\n"
                text = text + "</effect>\n"
            text = text + "</case>\n"
    text = text + "\n</rule>\n</model>\n"
    file.write(text)
    

def writePlainParams():
    file = open("params_unstructured.xml", 'w')
    text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    text = "\n<parameters>\n\n"
    text = text + prelude
    for a in a_m:
        for i in i_u:
            param = "theta_(a_m="+a + "^i_u="+i + ")"
            text = text + "<variable id=\"" + param + "\">\n"
            text = text + "<distrib type=\"dirichlet\">\n"
            for u in a_u:
                if a == "AskRepeat" and u == i:
                    text = text + "<alpha>3</alpha>\n"
                elif "Confirm(" in a and i in a and u == "Confirm":
                    text = text + "<alpha>3</alpha>\n"
                elif "Confirm(" in a and i not in a and u == "Disconfirm":
                    text = text + "<alpha>3</alpha>\n"
                elif u==i:
                    text = text + "<alpha>2</alpha>\n"
                else:
                    text = text + "<alpha>1</alpha>\n"
            text = text + "</distrib>\n</variable>\n\n"
    text = text + "\n</parameters>\n"
    file.write(text)
    
    
def writeLinearParams():
    file = open("params_linear.xml", 'w')
    text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
    text = "\n<parameters>\n\n"
    text = text + prelude 
    for a in a_m:
        for u in a_u:     
            param = "theta_(a_m="+a + "^a_u="+u+")"
            text = text + "<variable id=\"" + param + "\">\n"
            if a == "AskRepeat" and u in i_u:
                text = text + "<distrib type=\"uniform\">\n<min>0</min>\n<max>2</max>\n"    
            elif "Confirm(" in a and (u=="Confirm" or u=="Disconfirm") :
                text = text + "<distrib type=\"uniform\">\n<min>0</min>\n<max>3</max>\n"                               
            else:
                text = text + "<distrib type=\"uniform\">\n<min>0</min>\n<max>1</max>\n"
            text = text + "</distrib>\n</variable>\n\n"
    for i in i_u:
        for u in a_u:     
            param = "theta_(i_u="+i + "^a_u="+u+")"
            text = text + "<variable id=\"" + param + "\">\n"
            if i == u:
                text = text + "<distrib type=\"uniform\">\n<min>0</min>\n<max>3</max>\n"
            else:
                text = text + "<distrib type=\"uniform\">\n<min>0</min>\n<max>1</max>\n"
            text = text + "</distrib>\n</variable>\n\n"
    text = text + "\n</parameters>\n"
    file.write(text)


writePlainUserModel()
writeLinearUserModel()
writePlainParams()
writeLinearParams()
