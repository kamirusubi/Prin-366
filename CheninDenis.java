
/*  
В Magic: the Gathering существует 6 типов маны: белая, синяя, чёрная, красная, зелёная и бесцветная.
Стоимость карты может быть записана комбинацией соответствующих этим типам символов (символы могут повторяться). 
При записи мана-стоимости карты возможно добавление одного целого числа (обозначим его как n) строго в начале - 
это означает, что, помимо всей маны с конкретно обозначенным типом, игрок при розыгрыше карты обязан заплатить n ед. маны любых типов.
В речи такую ману обозначают словом 'generic', т. е. "общей".

Далеко не все игроки понимают принципы записи подобных мана-стоимостей. 
Мы пока не погружаемся в более сложные аспекты, такие как: 
    - гибридная мана; 
    - фирексийская мана; 
    - символы "Х", которые позволяют игроку заплатить любое целое неотрицательное количество общей маны (в т. ч. 0), 
    - и другие,
потому что даже на поверхностном уровне существует проблема.

В данном модуле на вход принимается любая комбинация символов "W", "U", "B", "R", "G" и "C" в любом регистре, а также любое неотрицательное число в начале;
Внутри обрабатывается и сохраняется информация о требуемых к оплате карты типах маны и её количестве.

Можно запросить информацию о полной мана-стоимости карты, при этом мана-символы группируются по определённым правилам и подаются на выход.
Также можно узнать, сколько всего маны необходимо заплатить для оплаты стоимости. Это называется конвертированной, или преобразованной, мана-стоимостью,
при этом тип маны не учитывается.
*/


import java.util.Map;

/* 
Общая мана всегда пишется в начале,
Бесцветная мана всегда пишется перед цветной,
А запись цветной маны зависит от того, какие из типов являются частью стоимости.
*/

/*
5 цветов образуют круг: W -> U -> B -> R -> G -> W - 
и в то же время образуют очередь: W -> U -> B -> R -> G
Цвета записываются в том порядке, при котором расстояние в круге между каждыми двумя из них наименьшее;
Если возникает неопределённость, то предпочтение отдаётся той записи, первый символ которой максимально близок к началу очереди

Пример: если пользователь введёт "urw", то при повторном запросе он получит запись "WUR", потому что при неопределённости между "RWU" и "WUR" победит запись с "W" в начале.
*/

// Pip - жаргонное обозначение одного мана-символа
public class ColoredPip {

    // Символ, которым обозначается пип
    private String symbol;
    
    // Количество пипов одного цвета
    private int number = 0;

    // позиция элемента в очереди и в круге
    private int index;
    // повторная позиция элемента в круге
    private int secondIndex;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getNumber() {
        return number;
    }

    protected void setNumber(int number) {
        this.number = number;
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getSecondIndex() {
        return secondIndex;
    }

    public void setSecondIndex(int secondIndex) {
        this.secondIndex = secondIndex;
    }

    private ColoredPip(int index, String symbol) {
        setIndex(index % 5);
        setSymbol(symbol);
        setSecondIndex(index + 5);
    }
}

public class Cost {

    private int generic, colorless;

    private ColoredPip white = new ColoredPip(0, "W");
    private ColoredPip blue = new ColoredPip(1, "U");
    private ColoredPip black = new ColoredPip(2, "B");
    private ColoredPip red = new ColoredPip(3, "R");
    private ColoredPip green = new ColoredPip(4, "G");

    public int getGeneric() {
        return generic;
    }

    private void setGeneric(int generic) {
        this.generic = generic;
    }

    public int getWhite() {
        return white.getNumber();
    }

    private void setWhite(int i) {
        this.white.setNumber(i);
    }

    public int getBlue() {
        return blue.getNumber();
    }

    private void setBlue(int i) {
        this.blue.setNumber(i);
    }

    public int getBlack() {
        return black.getNumber();
    }

    private void setBlack(int i) {
        this.black.setNumber(i);
    }

    public int getRed() {
        return red.getNumber();
    }

    private void setRed(int i) {
        this.red.setNumber(i);
    }

    public int getGreen() {
        return green.getNumber();
    }

    private void setGreen(int i) {
        this.green.setNumber(i);
    }

    public int getColorless() {
        return colorless;
    }

    private void setColorless(int i) {
        this.colorless = i;
    }

    public Cost(String cost){
        char[] chars = cost.toCharArray();
        CheckFirstPip(chars);
        for (int i = 1; i < chars.length; i++) {
            CheckPip(chars, i);
        }
    }

    // Проверка, является ли заданный символ обозначением маны конкретного типа
    private boolean NoteNonGeneric(char c) {
        if(c == 'W' || c == 'w'){
            setWhite(getWhite() + 1);
            return true;
        }
        else if(c == 'U' || c == 'u'){
            setBlue(getBlue() + 1);
            return true;
        }
        else if(c == 'B' || c == 'b'){
            setBlack(getBlack() + 1);
            return true;
        }
        else if(c == 'R' || c == 'r'){
            setRed(getRed() + 1);
            return true;
        }
        else if(c == 'G' || c == 'g'){
            setGreen(getGreen() + 1);
            return true;
        }
        else if(c == 'C' || c == 'c'){
            setColorless(getColorless() + 1);
            return true;
        }
        return false;
    }

    // При обработке символа необходимо учитывать, может ли он являться частью записи общей маны или нет
    private void CheckFirstPip(char[] chars) {
        char c = chars[0];
        if(!NoteNonGeneric(c)) {
            if((int)c - 48 <= 9) {
                setGeneric((int)c - 48);
            }
            else throw new IllegalArgumentException("Стоимость заклинания задана неверно");
        }
    }

    // При обработке символа необходимо учитывать, может ли он являться частью записи общей маны или нет
    private void CheckPip(char[] chars, int i) {
        char c = chars[i];
        char prev = chars[i-1];
        if(!NoteNonGeneric(c)) {
            if((int)c - 48 <= 9) {
                if((int)prev - 48 <= 9) {
                    setGeneric(getGeneric() * 10 + (int)c - 48);
                }
                else throw new IllegalArgumentException("Стоимость заклинания задана неверно");
            }
        }
    }

    // Получаем преобразованную мана-стоимость
    public int getConvertedManaCost() {
        return getGeneric() + getWhite() + getBlue() + getBlack() + getRed() + getGreen() + getColorless();
    }

    // Получаем полную мана-стоимость
    public String getFullManaCost() {

        String toPrint = "";

        // Запись общей маны
        if (generic > 0) toPrint = toPrint.concat(String.valueOf(generic));

        // Запись бесцветной маны
        for(int i = 0; i < colorless; i++)
            toPrint = toPrint.concat("C");

        // Запись общей маны
        Map<Integer, ColoredPip> pips = Map.ofEntries(
                Map.entry(0, white),
                Map.entry(1, blue),
                Map.entry(2, black),
                Map.entry(3, red),
                Map.entry(4, green)
        );

        // Обход круга и определения порядка записи цветной маны
        ArrayList<ColoredPip> indexes = new ArrayList<>();
        for(int i = 0; i < 5; i++) {
            ColoredPip pip = pips.get(i);
            if(pip.getNumber() > 0) {
                if(indexes.isEmpty())
                    indexes.add(pip);
                else {
                    ColoredPip pipFirst = indexes.getFirst();
                    ColoredPip pipLast = indexes.getLast();
                    if(i - pipLast.getIndex() <= pipFirst.getSecondIndex() - i)
                        indexes.addLast(pip);
                    else
                        indexes.addFirst(pip);
                }
            }
        }
        
        // Запись цветной маны
        for(ColoredPip pip : indexes) {
            for(int i = 0; i < pip.getNumber(); i++)
                toPrint = toPrint.concat(pip.getSymbol());
        }

        return toPrint;
    }
}

void main() {
    Cost cost;
    cost = new Cost("1"); // 1
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("10"); // 10
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("1W"); // 1W
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("WU"); // WU
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("UW"); // WU
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("UwU"); // WUU
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("WUG"); // GWU
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("urw"); // WUR
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("grUBw"); // WUBRG
    IO.println(String.format(cost.getFullManaCost()));
    cost = new Cost("bubr"); // UBBR
    IO.println(String.format(cost.getFullManaCost()));
    try{
        cost = new Cost("ipwyi"); // ошибка
    } catch(IllegalArgumentException e) {
        IO.println(String.format("ошибка"));
    }
    try{
        cost = new Cost("w1u"); // ошибка
    } catch(IllegalArgumentException e) {
        IO.println(String.format("ошибка"));
    }
}

