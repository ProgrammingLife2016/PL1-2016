
    public static void main(String[] args) {
        TokenIterator it = new TokenIterator("cbbaaacabaa", "bc");
        while (it.hasNext()) {
            System.out.println("next: " + it.next());
        }
    }

