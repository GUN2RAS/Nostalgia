package net.nostalgia;

public class TestAlpha {
    public static void main(String[] args) {
        net.nostalgia.alphalogic.gen.AlphaLevelSource source = new net.nostalgia.alphalogic.gen.AlphaLevelSource(0L);
        byte[] blocks = new byte[32768];
        source.provideChunk(7, 1, blocks);
        
        int solidCount = 0;
        int maxZ = 0;
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i] != 0) {
                solidCount++;
            }
        }
        System.out.println("Solid blocks generated: " + solidCount);
    }
}
