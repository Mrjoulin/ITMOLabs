package Lab03.utils.properties;

public enum CharactersActions {
    SAID{
        @Override
        public String getAction(boolean singular) {
            return singular ? "сказал" : "сказали";
        }
    },
    STAY{
        @Override
        public String getAction(boolean singular) {
            return singular ? "остался" : "остались";
        }
    },
    BEGIN{
        @Override
        public String getAction(boolean singular) {
            return singular ? "принялся" : "принялись";
        }
    },
    ENDED_UP{
        @Override
        public String getAction(boolean singular) {
            return singular ? "оказался" : "оказались";
        }
    },
    WALKED_AWAY{
        @Override
        public String getAction(boolean singular) {
            return singular ? "отошёл" : "отошли";
        }
    },
    MOCK{
        @Override
        public String getAction(boolean singular) {
            return singular ? "посмеивался втихомолку и делал вид, что не слышит вопросов" :
                    "посмеивались втихомолку и делали вид, что не слышат вопросов";
        }
    },
    CONFESSED{
        @Override
        public String getAction(boolean singular) {
            return singular ? "признался, что нашёл" : "признались, что нашли";
        }
    },
    POP_FROM_BACKPACK{
        @Override
        public String getAction(boolean singular) {
            return singular ? "вытряхнул из своего рюкзака" : "вытряхнули из своих рюкзаков";
        }
    };

    public abstract String getAction(boolean singular);

    @Override
    public String toString() {
        return getAction(true);
    }
}
