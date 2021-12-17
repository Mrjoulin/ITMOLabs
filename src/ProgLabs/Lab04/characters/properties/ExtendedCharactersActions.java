package Lab04.characters.properties;

public enum ExtendedCharactersActions {
    SEE {
        @Override
        public String getAction(boolean singular) {
            return singular ? "увидел" : "увидели";
        }
    },
    CANT {
        @Override
        public String getAction(boolean singular) {
            return singular ? "не мог" : "не могли";
        }
    },
    ABLE_TO_DO {
        @Override
        public String getAction(boolean singular) {
            return singular ? "смог" : "смогли";
        }
    },
    EXPECTED {
        @Override
        public String getAction(boolean singular) {
            return singular ? "ожидал" : "ожидали";
        }
    },
    TRIED {
        @Override
        public String getAction(boolean singular) {
            return singular ? "старался" : "старались";
        }
    };

    public abstract String getAction(boolean singular);

    @Override
    public String toString() {
        return getAction(true);
    }
}
