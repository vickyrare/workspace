namespace Events
{
    class Program
    {
        static void Main(string[] args)
        {
            var video = new Video() {Title = "Video 1"};
            VideoEncoder videoEncoder = new VideoEncoder();
            var mailServce = new MailService();
            var messageService = new MessageService();

            videoEncoder.VideoEncoded += mailServce.OnVideoEncoded;
            videoEncoder.VideoEncoded += messageService.OnVideoEncoded;

            videoEncoder.Encode(video);
        }
    }
}
